public class Tile {
    private int row;
    private int column;

    public Tile(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public void move() {
        row++;
    }
    public boolean ifConflict(Tile q){
        if(row == q.getRow() || column==q.getColumn()){
            return true;
        }else if(Math.abs(column-q.getColumn())==Math.abs(row-q.getRow())){
            return true;
        }
        return false;
    }

    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }


    @Override
    public String toString() {
        return "Tile{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}