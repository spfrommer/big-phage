package test.robot;

/**
 * An immutable Matrix class.
 */
public class Matrix {
	private float[] m_values;
	private int m_rows;
	private int m_columns;

	public Matrix(int rows, int columns) {
		m_values = new float[rows * columns];
		m_rows = rows;
		m_columns = columns;
	}

	public Matrix(int rows, int columns, float[] values) {
		if (rows * columns != values.length)
			throw new DimensionException();

		m_values = values.clone();
		m_rows = rows;
		m_columns = columns;
	}

	/**
	 * @return the Matrix cast to a Vector3d
	 */
	public MVector toVector() {
		if (m_rows == 2 && m_columns == 1) {
			return new MVector(getVal(0, 0), getVal(1, 0));
		} else {
			throw new DimensionException();
		}
	}

	/**
	 * @param row
	 * @param column
	 * @return the value at the row and columns
	 */
	public float getVal(int row, int column) {
		return m_values[row * m_columns + column];
	}

	/**
	 * @return the number of rows
	 */
	public int getRows() {
		return m_rows;
	}

	/**
	 * @return the number of columns
	 */
	public int getColumns() {
		return m_columns;
	}

	/**
	 * @param matrix
	 * @return the sum of the two Matrices
	 */
	public Matrix add(Matrix matrix) {
		if (this.getRows() != matrix.getRows() || this.getColumns() != matrix.getColumns())
			throw new DimensionException();

		float[] values = new float[m_rows * m_columns];
		for (int i = 0; i < values.length; i++) {
			values[i] = m_values[i] + matrix.m_values[i];
		}

		return new Matrix(m_rows, m_columns, values);
	}

	/**
	 * @param matrix
	 * @return the difference of the two Matrices
	 */
	public Matrix subtract(Matrix matrix) {
		if (this.getRows() != matrix.getRows() || this.getColumns() != matrix.getColumns())
			throw new DimensionException();

		float[] values = new float[m_rows * m_columns];
		for (int i = 0; i < values.length; i++) {
			values[i] = m_values[i] - matrix.m_values[i];
		}

		return new Matrix(m_rows, m_columns, values);
	}

	/**
	 * @param scalar
	 * @return the result of a scalar multiply between this Matrix and the scalar
	 */
	public Matrix scalarMultiply(float scalar) {
		float[] values = new float[m_rows * m_columns];
		for (int i = 0; i < values.length; i++) {
			values[i] = m_values[i] * scalar;
		}

		return new Matrix(m_rows, m_columns, values);
	}

	/**
	 * @param matrix
	 * @return the product of the two Matrices
	 */
	public Matrix multiply(Matrix matrix) {
		if (this.getColumns() != matrix.getRows())
			throw new DimensionException();

		int rows = this.getRows();
		int columns = matrix.getColumns();
		float[] newArray = new float[this.getRows() * matrix.getColumns()];

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				float dotProduct = 0;
				for (int i = 0; i < this.getColumns(); i++) {
					dotProduct += this.getVal(r, i) * matrix.getVal(i, c);
				}
				newArray[r * columns + c] = dotProduct;
			}
		}

		return new Matrix(rows, columns, newArray);
	}

	/**
	 * Adds another row of 1's.
	 * 
	 * @return the augmented Matrix
	 */
	public Matrix augment() {
		int rows = this.getRows();
		int columns = this.getColumns();
		float[] newArray = new float[(rows + 1) * columns];

		System.arraycopy(m_values, 0, newArray, 0, m_values.length);
		for (int i = rows * columns; i < (rows + 1) * columns; i++) {
			newArray[i] = 1;
		}
		return new Matrix(rows + 1, columns, newArray);
	}

	/**
	 * @return the transpose
	 */
	public Matrix transpose() {
		int rows = this.getColumns();
		int columns = this.getRows();
		float[] newArray = new float[rows * columns];

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				newArray[r * columns + c] = this.getVal(c, r);
			}
		}

		return new Matrix(rows, columns, newArray);
	}

	/**
	 * Creates a sub matrix excluding a row and a column.
	 * 
	 * @param excludingRow
	 * @param excludingColumn
	 * @return
	 */
	public Matrix createSubMatrix(int excludingRow, int excludingColumn) {
		int rows = this.getRows() - 1;
		int columns = this.getColumns() - 1;
		float[] newArray = new float[rows * columns];

		int rcount = -1;
		for (int r = 0; r < rows + 1; r++) {
			if (r == excludingRow)
				continue;
			rcount++;
			int ccount = -1;
			for (int c = 0; c < columns + 1; c++) {
				if (c == excludingColumn)
					continue;
				newArray[rcount * columns + ++ccount] = this.getVal(r, c);
			}
		}
		return new Matrix(rows, columns, newArray);
	}

	public Matrix cofactor() {
		int rows = this.getRows();
		int columns = this.getColumns();
		float[] newArray = new float[rows * columns];

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				newArray[r * columns + c] = MathUtils.changeSign(r) * MathUtils.changeSign(c)
						* this.createSubMatrix(r, c).getDeterminant();
			}
		}

		return new Matrix(rows, columns, newArray);
	}

	/**
	 * Returns the inversion of the Matrix. Must be a square matrix.
	 * 
	 * @return the inverted Matrix
	 */
	public Matrix invert() {
		if (this.getRows() != this.getColumns())
			throw new DimensionException();

		return this.cofactor().transpose().scalarMultiply(1 / this.getDeterminant());
	}

	/**
	 * Returns the determinant of the matrix. Must be square.
	 * 
	 * @return the determinant
	 */
	public float getDeterminant() {
		if (this.getRows() != this.getColumns())
			throw new DimensionException();

		int rows = this.getRows();
		int columns = this.getColumns();

		if (rows == 1)
			return this.getVal(0, 0);

		if (rows == 2)
			return (this.getVal(0, 0) * this.getVal(1, 1)) - (this.getVal(0, 1) * this.getVal(1, 0));

		float sum = 0;
		for (int i = 0; i < columns; i++) {
			sum += MathUtils.changeSign(i) * this.getVal(0, i) * this.createSubMatrix(0, i).getDeterminant();
		}

		return sum;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int r = 0; r < m_rows; r++) {
			s.append("[");
			for (int c = 0; c < m_columns; c++) {
				if (c == 0) {
					s.append(this.getVal(r, c));
				} else {
					s.append(", " + this.getVal(r, c));
				}
			}
			s.append("]\n");
		}

		return s.toString();
	}

	public static void main(String[] args) {
		Matrix m1 = new Matrix(4, 4, new float[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 });
		Matrix m2 = new Matrix(4, 4, new float[] { 1, 2, 3, 4, 8, 1, 9, 2, 3, 0, 1, 0, 0, 0, 0, 1 });
		Matrix m3 = new Matrix(3, 3, new float[] { 1, 2, 3, 3, 2, 1, 2, 1, 3 });
		// System.out.println(m1.add(m2));

		// System.out.println(m2);
		// System.out.println(m2.getDeterminant());
		System.out.println(m3.invert());
	}
}