package net.varkhan.core.management.state.lifecycle;

import net.varkhan.core.management.state.State;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/31/11
 * @time 12:22 AM
 */
public enum LifeState implements State<LifeLevel,LifeState> {

    STOPPED {
        @Override
        public LifeState[] transition(LifeLevel level) {
            return new LifeState[] { STARTING };
        }
    },
    STARTING {
        @Override
        public LifeState[] transition(LifeLevel level) {
            return new LifeState[] { RUNNING, STOPPED };
        }
    },
    RUNNING {
        @Override
        public LifeState[] transition(LifeLevel level) {
            return new LifeState[] { STOPPING, SUSPENDED };
        }
    },
    SUSPENDED {
        @Override
        public LifeState[] transition(LifeLevel level) {
            return new LifeState[] { RUNNING };
        }
    },
    STOPPING {
        @Override
        public LifeState[] transition(LifeLevel level) {
            return new LifeState[] { STOPPED };
        }
    },
    ;

    @Override
    public LifeState aggregate(LifeState state, LifeLevel level) {
        return this;
    }

}
