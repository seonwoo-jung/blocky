package game;

public enum Action {
    SMASH,
    SWAP_VERTICALLY,
    SWAP_HORIZONTALLY,
    TURN_CW,
    TURN_CCW;

    public boolean runAction(Block b) {
        switch (this) {
            case SMASH:
                return b.smash();

            case SWAP_VERTICALLY:
                return b.swap(true);

            case SWAP_HORIZONTALLY:
                return b.swap(false);

            case TURN_CW:
                return b.rotate(true);
            case TURN_CCW:
                return b.rotate(false);
        }
        return false;
    }

    public Runnable getMethod(Block b) {
        switch (this) {
            case SMASH:
                return () -> b.smash();

            case SWAP_VERTICALLY:
                return () -> b.swap(true);

            case SWAP_HORIZONTALLY:
                return () -> b.swap(false);

            case TURN_CW:
                return () -> b.rotate(true);

            case TURN_CCW:
                return () -> b.rotate(false);

            default:
                return null;
        }
    }

    public boolean runInverse(Block b) {
        // you don't have to use this method if you don't want to
        // but it can be useful for undoing actions...
        return false;
    }
}
