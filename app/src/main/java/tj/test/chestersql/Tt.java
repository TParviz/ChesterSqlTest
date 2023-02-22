package tj.test.chestersql;

public class Tt {
    private final long id;
    private final String name;
    private final long parentId;
    private final int countIds;

    public Tt(long id, String name, long parentId, int countIds) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.countIds = countIds;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getParentId() {
        return parentId;
    }

    public int getCountIds() {
        return countIds;
    }
}