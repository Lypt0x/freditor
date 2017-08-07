package freditor.vector;

final class IntVector3 extends IntVectorN {
    private final int[][][] root;
    private final int[] tail;

    IntVector3(int[][] full, int x) {
        super(32 * 32 + 1);

        root = new int[32][][];
        root[0] = full;
        root[1] = new int[32][];
        root[1][0] = tail = array32of(x);
    }

    private IntVector3(int length, int[][][] root, int[] tail) {
        super(length);
        this.root = root;
        this.tail = tail;
    }

    IntVector3(int length, int[][][] root) {
        super(length);
        this.root = root;
        this.tail = leaf(length - 1);
    }

    @Override
    protected int[] leaf(int i) {
        return root[i >>> 10][(i >>> 5) & 31];
    }

    @Override
    public int intAt(int index) {
        return leaf(index)[index & 31];
    }

    @Override
    public IntVector push(int x) {
        int a = length & 31;
        if (tail[a] == 0) {
            tail[a] = x;
            return new IntVector3(length + 1, root, tail);
        }
        if (length == 32 * 32 * 32) return new IntVector4(root, x);

        int b = (length >>> 5) & 31;
        int c = length >>> 10;

        int[][][] C = root;
        int[][] B = lazy(C[c]);
        int[] A = lazy(B[b]);

        return new IntVector3(length + 1, with(C, c, with(B, b, with(A, a, x))));
    }

    @Override
    public IntVector pop() {
        if (length == 32 * 32 + 1) return new IntVector2(length - 1, root[0]);
        return new IntVector3(length - 1, root);
    }

    @Override
    public IntVector take(int n) {
        if (n == length) return this;
        if (n == 0) return IntVector.empty;
        if (n <= 32) return new IntVector1(n, root[0][0]);
        if (n <= 32 * 32) return new IntVector2(n, root[0]);
        return new IntVector3(n, root);
    }
}
