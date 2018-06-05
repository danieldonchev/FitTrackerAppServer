package tracker.SQLBuilderHelper;

public class SQLBuilder {

    private static final int INSERT = 1;
    private static final int DELETE = 2;
    private static final int UPDATE = 3;
    private static final int SELECT = 4;

    private StringBuilder statement;
    private String table;
    private String where[];
    private String orderBy[];
    private String columns[];
    private String values[];
    private int limit;
    private boolean updateOnDuplicate;

    private int operation;

    public SQLBuilder() {
        statement = new StringBuilder();
        updateOnDuplicate = false;
    }

    public SQLBuilder table(String table) {
        this.table = table;
        return this;
    }

    public SQLBuilder select(String... columns) {
        this.operation = SELECT;
        this.columns = columns;
        return this;
    }

    public SQLBuilder insert(String[] columns, String... values) {
        this.operation = INSERT;
        this.columns = columns;
        this.values = values;
        return this;
    }

    public SQLBuilder update(String[] columns, String... values) {
        this.operation = UPDATE;
        this.columns = columns;
        this.values = values;
        return this;
    }

    public SQLBuilder delete() {
        this.operation = DELETE;
        return this;
    }

    public SQLBuilder where(String... selectionArgs) {
        this.where = selectionArgs;
        return this;
    }

    public SQLBuilder orderBy(String... selectionArgs) {
        this.orderBy = selectionArgs;
        return this;
    }

    public SQLBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    public SQLBuilder updateOnDuplicate(boolean shouldUpdate) {
        this.updateOnDuplicate = shouldUpdate;
        return this;
    }

    public String build() {
        switch (operation) {
            case INSERT: {
                try {
                    return buildInsert();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case DELETE: {
                return buildDelete();
            }
            case SELECT: {
                return buildSelect();
            }
            case UPDATE: {
                try {
                    return buildUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        throw new UnsupportedOperationException();
    }

    private String buildInsert() throws Exception {
        if (columns.length == values.length) {
            if (table != null & table.length() > 0) {
                statement.append("INSERT INTO " + table);
                statement.append("(" + stringConcatinatorWithComma(columns) + ") VALUES (");
                statement.append(stringConcatinatorWithComma(values) + ")");
                if (updateOnDuplicate) {
                    statement.append(" ON DUPLICATE KEY UPDATE ");
                    for (int i = 0; i < values.length; i++) {
                        statement.append(columns[i] + "=VALUES(" + columns[i] + ")");
                        if (i == values.length - 1) {
                            break;
                        } else {
                            statement.append(",");
                        }
                    }
                }
                statement.append(";");
            } else {
                throw new NullPointerException("Table name is null or size is 0");
            }
        } else {
            throw new Exception("Columns and values not equal in size");
        }

        return statement.toString();
    }

    private String buildSelect() {
        statement.append("SELECT ");
        statement.append(stringConcatinatorWithComma(columns));
        statement.append(" FROM " + table);
        addWhere();
        addOrderBy();
        addLimit();
        return statement.toString();
    }

    private String buildDelete() {
        statement.append("DELETE");
        statement.append(" FROM " + table);
        addWhere();
        addOrderBy();
        addLimit();
        return statement.toString();
    }

    private String buildUpdate() throws Exception {
        if (columns.length == values.length) {
            statement.append("UPDATE " + table);
            statement.append(" SET ");
            for (int i = 0; i < columns.length; i++) {
                statement.append(columns[i]);
                statement.append("=");
                statement.append(values[i]);
                if (i == columns.length - 1) {
                    break;
                }
                statement.append(",");
            }
            addWhere();
            addOrderBy();
        } else {
            throw new Exception("Columns and values not equal in size");
        }

        return statement.toString();
    }

    private void addWhere() {
        if (where != null) {
            statement.append(" WHERE ");
            statement.append(stringConcatinator(where));
        }
    }

    private void addOrderBy() {
        if (orderBy != null) {
            statement.append(" ORDER BY ");
            statement.append(stringConcatinatorWithComma(orderBy));
        }
    }

    private void addLimit() {
        if (limit > 0) {
            statement.append(" LIMIT ");
            statement.append(String.valueOf(limit));
        }
    }

    private String stringConcatinatorWithComma(String... array) {
        StringBuilder builder = new StringBuilder();
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                builder.append(array[i]);

                if (i == array.length - 1) {
                    break;
                }
                builder.append(",");
            }
        } else {
            throw new NullPointerException("columns is null");
        }

        return builder.toString();
    }

    private String stringConcatinator(String... array) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i == array.length - 1) {
                break;
            }
            builder.append(" ");
        }

        return builder.toString();
    }
}
