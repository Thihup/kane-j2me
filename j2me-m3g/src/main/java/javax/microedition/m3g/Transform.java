package javax.microedition.m3g;

public class Transform {
    private float[] matrix = new float[16];
    public Transform() {
        setIdentity();
    }
    public Transform(Transform transform) {
        System.arraycopy(transform.matrix, 0, this.matrix, 0, 16);
    }
    public void setIdentity() {
        for (int i = 0; i < 16; i++) matrix[i] = 0;
        matrix[0] = matrix[5] = matrix[10] = matrix[15] = 1.0f;
    }
}
