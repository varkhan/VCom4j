package net.varkhan.core.management.state;

import junit.framework.TestCase;
import net.varkhan.core.management.state.health.HealthLevel;
import net.varkhan.core.management.state.health.HealthState;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/12/11
 * @time 12:22 PM
 */
public class StateReportTest extends TestCase {

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
}
