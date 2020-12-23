package gold_miner;

/**
 * Class implementing the <b>square tile</b> comprising the board
 *
 * <p>Note that the row and column numbers in this class (as well as in all other classes
 * responsible for the back-end behavior) are zero-based.</p>
 */
public class Square {
    /* Row-coordinate of the square tile */
    private int row;
    /* Column-coordinate of the square tile */
    private int col;

    /* Designation of the square tile (empty, beacon, pit, or gold) */
    private char designation;
    /* true if the square tile has already been visited by the miner; false, otherwise */
    private boolean isVisited;

    /**
     * Creates a square tile object given the row number, column number, and designation
     * of the tile
     *
     * @param row row number of this square tile
     * @param col column number of this square tile
     * @param designation designation of this square tile (empty, beacon, pit, or gold)
     */
    public Square(int row, int col, char designation) {
        this.row = row;
        this.col = col;
        this.designation = designation;

        /* A tile is initially unvisited. */
        this.isVisited = false;
    }

    /**
     * Returns the row number of this square tile
     *
     * @return row number of this square tile
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column number of this square tile
     *
     * @return column number of this square tile
     */
    public int getCol() {
        return col;
    }

    /**
     * Returns the designation of this square tile
     *
     * <p>The following are the possible designations of this square tile:</p>
     * <ul>
     *     <li><b>Empty</b> - It is a regular tile with no special effects whatsoever.</li>
     *     <li><b>Beacon</b> - It returns the distance between this tile and the gold tile,
     *     provided that the gold tile is in one of its four cardinal directions and that
     *     there is no pit in between this tile and the gold tile. </li>
     *     <li><b>Pit</b> - It leads to a losing game-over situation should the miner step on this tile.</li>
     *     <li><b>Gold</b> - It leads to a winning game-over situation should the miner step on this tile.</li>
     * </ul>
     *
     * @return designation of this square tile
     */
    public char getDesignation() {
        return designation;
    }

    /**
     * Returns <code>true</code> if this square tile has already been visited by the miner;
     * <code>false</code>, otherwise
     *
     * @return <code>true</code> if this square tile has already been visited by the miner;
     * <code>false</code>, otherwise
     */
    public boolean getIsVisited() {
        return isVisited;
    }

    /**
     * Sets the designation of this square tile to the specified designation
     *
     * <p>This method should only be invoked once, that is, during the creation of the <code>Board</code>
     * object. Once the designation has been set, then it is already immutable.</p>
     *
     * <p>The following are the possible designations of this square tile:</p>
     * <ul>
     *     <li><b>Empty</b> - It is a regular tile with no special effects whatsoever.</li>
     *     <li><b>Beacon</b> - It returns the distance between this tile and the gold tile,
     *     provided that the gold tile is in one of its four cardinal directions and that
     *     there is no pit in between this tile and the gold tile. </li>
     *     <li><b>Pit</b> - It results in a losing game-over situation should the miner step on this tile.</li>
     *     <li><b>Gold</b> - It results in a winning game-over situation should the miner step on this tile.</li>
     * </ul>
     *
     * @param designation specified designation of this square tile
     */
    public void setDesignation(char designation) {
        this.designation = designation;
    }

    /**
     * Marks this square tile as visited by the miner
     *
     * <p>The effect of this method is irreversible, that is, once this square tile is marked
     * as visited, then it cannot be "unvisited."</p>
     */
    public void visit() {
        isVisited = true;
    }

    /**
     * Returns <code>true</code> if the given square tile is found at the immediate left
     * of this square tile; <code>false</code>, otherwise
     *
     * @param other square tile whose position relative to this square tile is determined
     * @return <code>true</code> if the given square tile is found at the immediate left
     * of this square tile; <code>false</code>, otherwise
     */
    public boolean isOtherLeft(Square other) {
        return other.col == col - 1;
    }

    /**
     * Returns <code>true</code> if the given square tile is found at the immediate right
     * of this square tile; <code>false</code>, otherwise
     *
     * @param other square tile whose position relative to this square tile is determined
     * @return <code>true</code> if the given square tile is found at the immediate right
     * of this square tile; <code>false</code>, otherwise
     */
    public boolean isOtherRight(Square other) {
        return other.col == col + 1;
    }

    /**
     * Returns <code>true</code> if the given square tile is found immediately above
     * this square tile; <code>false</code>, otherwise
     *
     * @param other square tile whose position relative to this square tile is determined
     * @return <code>true</code> if the given square tile is found immediately above
     * this square tile; <code>false</code>, otherwise
     */
    public boolean isOtherUp(Square other) {
        return other.row == row - 1;
    }

    /**
     * Returns <code>true</code> if the given square tile is found immediately below
     * this square tile; <code>false</code>, otherwise
     *
     * @param other square tile whose position relative to this square tile is determined
     * @return <code>true</code> if the given square tile is found immediately below
     * this square tile; <code>false</code>, otherwise
     */
    public boolean isOtherDown(Square other) {
        return other.row == row + 1;
    }

    /**
     * Returns <code>true</code> if the row- and column-coordinates of the two square tiles
     * are equal; <code>false</code>, otherwise
     *
     * @param obj square tile being compared to this square tile
     * @return <code>true</code> if the row- and column-coordinates of the two square tiles
     * are equal; <code>false</code>, otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        Square other = (Square) obj;
        return other.row == row && other.col == col;
    }

    /**
     * Returns a string representation of this square tile
     *
     * <p>The string representation contains the following details in order:</p>
     * <ul>
     *     <li>row number</li>
     *     <li>column number</li>
     *     <li>designation</li>
     *     <li>whether it has been visited</li>
     * </ul>
     *
     * @return string representation of this square tile
     */
    @Override
    public String toString() {
        String detail = new String();

        detail += (row + 1) + " " + (col + 1) + " : ";
        if (designation == 'B')
            detail += "Beacon";
        else if (designation == 'P')
            detail += "Pit";
        else if (designation == 'G')
            detail += "Gold";
        else if (designation == 'O')
            detail += "Out Of Bounds";
        else
            detail += "Empty";

        return detail;
    }
}
