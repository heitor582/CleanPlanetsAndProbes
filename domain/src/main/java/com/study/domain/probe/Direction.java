package com.study.domain.probe;

public enum Direction {
    UP {
        @Override
        public Direction turnLeft() {
            return LEFT;
        }

        @Override
        public Direction turnRight() {
            return RIGHT;
        }
    },
    DOWN {
        @Override
        public Direction turnLeft() {
            return RIGHT;
        }

        @Override
        public Direction turnRight() {
            return LEFT;
        }
    },
    LEFT {
        @Override
        public Direction turnLeft() {
            return DOWN;
        }

        @Override
        public Direction turnRight() {
            return UP;
        }
    },
    RIGHT {
        @Override
        public Direction turnLeft() {
            return UP;
        }

        @Override
        public Direction turnRight() {
            return DOWN;
        }
    };

    public abstract Direction turnLeft();
    public abstract Direction turnRight();

    public boolean isRight() {
        return this.equals(RIGHT);
    }

    public boolean isLeft() {
        return this.equals(LEFT);
    }

    public boolean isDown() {
        return this.equals(DOWN);
    }

    public boolean isUp() {
        return this.equals(UP);
    }
}