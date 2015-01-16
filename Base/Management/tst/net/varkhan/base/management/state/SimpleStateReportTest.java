package net.varkhan.base.management.state;

import junit.framework.TestCase;
import net.varkhan.base.management.state.health.HealthLevel;
import net.varkhan.base.management.state.health.HealthState;
import net.varkhan.base.management.state.lifecycle.LifeLevel;
import net.varkhan.base.management.state.lifecycle.LifeState;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/12/11
 * @time 12:22 PM
 */
public class SimpleStateReportTest extends TestCase {

    public void testBasicHealthReport() {
        final int time[] = new int[1];
        StateCheck<HealthLevel,HealthState> major1 = new SimpleStateCheck<HealthLevel,HealthState>("MajTest-3.4","Major: fails at 3, recovers at 5",HealthLevel.MAJOR) {
            @Override
            public HealthState state() {
                switch(time[0]) {
                    case 0:
                    case 1:
                    case 2:
                        return HealthState.HEALTHY;
                    case 3:
                    case 4:
                        return HealthState.FAILED;
                    case 5:
                        return HealthState.HEALTHY;
                    default:
                        return HealthState.HEALTHY;
                }
            }

            @Override
            public String reason() {
                return ""+time[0];
            }

            @Override
            public void update() {
                // nothing to do
            }
        };

        StateCheck<HealthLevel,HealthState> minor2 = new SimpleStateCheck<HealthLevel,HealthState>("MinTest-2.3","Minor: fails at 2, recovers at 4",HealthLevel.MINOR) {
            @Override
            public HealthState state() {
                switch(time[0]) {
                    case 0:
                    case 1:
                        return HealthState.HEALTHY;
                    case 2:
                    case 3:
                        return HealthState.FAILED;
                    case 4:
                    case 5:
                        return HealthState.HEALTHY;
                    default:
                        return HealthState.HEALTHY;
                }
            }

            @Override
            public String reason() {
                return ""+time[0];
            }

            @Override
            public void update() {
                // nothing to do
            }
        };

        StateCheck<HealthLevel,HealthState> minor3 = new SimpleStateCheck<HealthLevel,HealthState>("MajTest-3","Minor: fails at 3, recovers at 4",HealthLevel.MINOR) {
            @Override
            public HealthState state() {
                switch(time[0]) {
                    case 0:
                    case 1:
                    case 2:
                        return HealthState.HEALTHY;
                    case 3:
                        return HealthState.FAILED;
                    case 4:
                    case 5:
                        return HealthState.HEALTHY;
                    default:
                        return HealthState.HEALTHY;
                }
            }

            @Override
            public String reason() {
                return ""+time[0];
            }

            @Override
            public void update() {
                // nothing to do
            }
        };

        SimpleStateReport<HealthLevel,HealthState> report = new SimpleStateReport<HealthLevel,HealthState>(HealthState.HEALTHY);
        report.add(major1);
        report.add(minor2);
        report.add(minor3);
        time[0] = 0;
        while(time[0]<10) {
            HealthState exp = HealthState.HEALTHY;
            switch(time[0]) {
                case 0:
                case 1:
                    exp =  HealthState.HEALTHY; break;
                case 2:
                    exp =  HealthState.UNSTABLE; break;
                case 3:
                case 4:
                    exp =  HealthState.FAILED; break;
                case 5:
                    exp =  HealthState.HEALTHY; break;
                default:
                    exp =  HealthState.HEALTHY; break;
            }
            assertEquals("Status at "+time[0],exp,report.state());
            time[0]++;
        }
    }

    public void testBasicLifeReport() {
        final int time[] = new int[1];
        StateCheck<LifeLevel,LifeState> major1 = new SimpleStateCheck<LifeLevel,LifeState>("MajTest-3.4","Major: starts at 1, is started at 2, stops at 4, is stopped at 5",LifeLevel.SYSTEM) {
            @Override
            public LifeState state() {
                switch(time[0]) {
                    case 0:
                        return LifeState.STOPPED;
                    case 1:
                        return LifeState.STARTING;
                    case 2:
                        return LifeState.RUNNING;
                    case 3:
                        return LifeState.RUNNING;
                    case 4:
                        return LifeState.STOPPING;
                    case 5:
                        return LifeState.STOPPED;
                    default:
                        return LifeState.STOPPED;
                }
            }

            @Override
            public String reason() {
                return ""+time[0];
            }

            @Override
            public void update() {
                // nothing to do
            }
        };

//        StateCheck<HealthLevel,HealthState> minor2 = new SimpleStateCheck<HealthLevel,HealthState>("MinTest-2.3","Minor: fails at 2, recovers at 4",HealthLevel.MINOR) {
//            @Override
//            public HealthState state() {
//                switch(time[0]) {
//                    case 0:
//                    case 1:
//                        return HealthState.HEALTHY;
//                    case 2:
//                    case 3:
//                        return HealthState.FAILED;
//                    case 4:
//                    case 5:
//                        return HealthState.HEALTHY;
//                    default:
//                        return HealthState.HEALTHY;
//                }
//            }
//
//            @Override
//            public String reason() {
//                return ""+time[0];
//            }
//
//            @Override
//            public void update() {
//                // nothing to do
//            }
//        };
//
//        StateCheck<HealthLevel,HealthState> minor3 = new SimpleStateCheck<HealthLevel,HealthState>("MajTest-3","Minor: fails at 3, recovers at 4",HealthLevel.MINOR) {
//            @Override
//            public HealthState state() {
//                switch(time[0]) {
//                    case 0:
//                    case 1:
//                    case 2:
//                        return HealthState.HEALTHY;
//                    case 3:
//                        return HealthState.FAILED;
//                    case 4:
//                    case 5:
//                        return HealthState.HEALTHY;
//                    default:
//                        return HealthState.HEALTHY;
//                }
//            }
//
//            @Override
//            public String reason() {
//                return ""+time[0];
//            }
//
//            @Override
//            public void update() {
//                // nothing to do
//            }
//        };

        SimpleStateReport<LifeLevel,LifeState> report = new SimpleStateReport<LifeLevel,LifeState>(LifeState.STOPPED);
        report.add(major1);
//        report.add(minor2);
//        report.add(minor3);
        time[0] = 0;
        while(time[0]<10) {
            LifeState exp = LifeState.STOPPED;
            switch(time[0]) {
                case 0:
                    exp = LifeState.STOPPED; break;
                case 1:
                    exp = LifeState.STARTING; break;
                case 2:
                    exp = LifeState.RUNNING; break;
                case 3:
                    exp = LifeState.RUNNING; break;
                case 4:
                    exp = LifeState.STOPPING; break;
                case 5:
                    exp = LifeState.STOPPED; break;
                default:
                    exp = LifeState.STOPPED; break;
            }
            assertEquals("Status at "+time[0],exp,report.state());
            time[0]++;
        }
    }
}
