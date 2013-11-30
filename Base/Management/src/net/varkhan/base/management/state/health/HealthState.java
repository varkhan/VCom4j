package net.varkhan.base.management.state.health;

import net.varkhan.base.management.state.State;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/12/11
 * @time 1:30 PM
 */
public enum HealthState implements State<HealthLevel,HealthState> {


    HEALTHY {
        @Override
        public HealthState aggregate(HealthState state, HealthLevel level) {
            switch(state) {
                case FAILED:
                    switch(level) {
                        case MAJOR:
                            return FAILED;
                        case MINOR:
                            return UNSTABLE;
                    }
                case UNSTABLE:
                    switch(level) {
                        case MAJOR:
                            return UNSTABLE;
                    }
            }
            return HEALTHY;
        }
    },

    UNSTABLE {
        @Override
        public HealthState aggregate(HealthState state, HealthLevel level) {
            switch(state) {
                case FAILED:
                    switch(level) {
                        case MAJOR:
                            return FAILED;
                    }
            }
            return UNSTABLE;
        }
    },

    FAILED {
        @Override
        public HealthState aggregate(HealthState state, HealthLevel level) {
            return FAILED;
        }
    },
    ;


    @Override
    public HealthState[] transition(HealthLevel level) {
        return new HealthState[] { HEALTHY, UNSTABLE, FAILED };
    }
}
