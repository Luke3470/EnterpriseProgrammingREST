package uk.ac.mmu.enterpriseprogrammingrest.model;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import uk.ac.mmu.enterpriseprogrammingrest.DB.DB;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookFilterDTO;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.BookVO;
import uk.ac.mmu.enterpriseprogrammingrest.model.data.QueryParts;
import uk.ac.mmu.enterpriseprogrammingrest.model.errors.DataAccessException;

public class BookDAOImpl implements BookDAO {

    private final DB db;

    public BookDAOImpl(DB db) {
        this.db = db;
    }

    @Override
    public List<BookVO> getBooks(BookFilterDTO filterDTO) {

        String baseSql = """
            SELECT id, title, author, date, genres, characters, synopsis, coverUrl
            FROM books
        """;

        QueryParts qp = buildBookFilter(filterDTO);
        qp.sql = baseSql + qp.sql + " LIMIT ? OFFSET ? ";
        qp.params.add(filterDTO.getLimit());
        qp.params.add(filterDTO.getOffset());

        List<BookVO> books = new ArrayList<>();

        try (
            Connection conn = db.createCon();
            PreparedStatement ps = conn.prepareStatement(qp.sql)
        ) {
            bindParams(ps, qp.params);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    books.add(mapToVO(rs));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch filtered books", e);
        }

        return books;
    }

    @Override
    public List<BookVO> getAllBooks() {
        List<BookVO> books = new ArrayList<>();
        String sql = "SELECT * FROM books";

        try (
            Connection conn = db.createCon();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)
        ) {
            while (rs.next()) {
                books.add(mapToVO(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to fetch All books", e);
        }

        return books;
    }


    @Override
    public BookVO getBook(int id) {
        String sql = "SELECT * FROM books WHERE id = ?";

        try (
            Connection conn = db.createCon();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapToVO(rs);
                }
            }

        } catch (SQLException e) {
           throw new DataAccessException("Failed to fetch book: "+ id, e);
        }

        return null;
    }


    @Override
    public void addBook(BookVO book) {
        String sql = """
            INSERT INTO books 
            (title, author, date, genres, characters, synopsis, coverUrl)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (
            Connection conn = db.createCon();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getDate());
            ps.setString(4, book.getGenres());
            ps.setString(5, book.getCharacters());
            ps.setString(6, book.getSynopsis());
            ps.setString(7, book.getCoverUrl());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw  new DataAccessException("Failed to add New book:" + book.getTitle(), e);
        }
    }

     @Override
    public void updateBook(BookVO book) {
        String sql = """
            UPDATE books 
            SET title=?, author=?, date=?, genres=?, characters=?, synopsis=?, coverUrl=? 
            WHERE id=?
        """;

        try (
            Connection conn = db.createCon();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getDate());
            ps.setString(4, book.getGenres());
            ps.setString(5, book.getCharacters());
            ps.setString(6, book.getSynopsis());
            ps.setString(7, book.getCoverUrl());
            ps.setInt(8, book.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update book: "+ book.getId(), e);
        }
    }

    @Override
    public int countBooks(BookFilterDTO filter) {

        String baseSql = "SELECT COUNT(*) FROM books";

        QueryParts qp = buildBookFilter(filter);
        qp.sql = baseSql + qp.sql;

        try (
            Connection conn = db.createCon();
            PreparedStatement ps = conn.prepareStatement(qp.sql)
        ) {
            bindParams(ps, qp.params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to count books", e);
        }

        return 0;
    }

    @Override
    public List<String> getGenres() {
        String sql = "SELECT DISTINCT TRIM(json.genre) AS genre\n"
            + "FROM cadmancl.books,\n"
            + "JSON_TABLE(\n"
            + "  CONCAT('[\"', REPLACE(genres, ', ', '\",\"'), '\"]'),\n"
            + "  '$[*]' COLUMNS (genre VARCHAR(255) PATH '$')\n"
            + ") AS json\n"
            + "ORDER BY genre;";

        List<String> genres = new ArrayList<>();
        try (
            Connection conn = db.createCon();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql)
        ) {
            while (rs.next()) {
                genres.add(rs.getString("genre"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Failed to Get Unique Genres", e);
        }
        return genres;
    }


    @Override
    public void deleteBook(int id) {
        String sql = "DELETE FROM books WHERE id = ?";

        try (
            Connection conn = db.createCon();
            PreparedStatement ps = conn.prepareStatement(sql)
        ) {
            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Failed to Delete books: "+id, e);
        }
    }


    private QueryParts buildBookFilter(BookFilterDTO filter) {

        StringBuilder sql = new StringBuilder(" WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (filter.getQuery() != null && !filter.getQuery().isBlank()) {
            sql.append(" AND title LIKE ? ");
            params.add("%" + filter.getQuery() + "%");
        }

        if (filter.getDateFrom() != null) {
            sql.append(" AND date >= ? ");
            params.add(filter.getDateFrom());
        }

        if (filter.getId() != null) {
            sql.append(" AND ID = ? ");
            params.add(filter.getId());
        }

        if (filter.getDateTo() != null) {
            sql.append(" AND date <= ? ");
            params.add(filter.getDateTo());
        }

        if (!filter.getGenres().isEmpty()) {
            sql.append(" AND (");

            for (int i = 0; i < filter.getGenres().size(); i++) {
                if (i > 0) sql.append(" OR ");
                sql.append("genres LIKE ? ");
                params.add("%" + filter.getGenres().get(i) + "%");
            }

            sql.append(")");
        }

        QueryParts qp = new QueryParts();
        qp.sql = sql.toString();
        qp.params = params;

        return qp;
    }

    private void bindParams(PreparedStatement ps, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }
    }

    private BookVO mapToVO(ResultSet rs) throws SQLException {
        return new BookVO(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("author"),
            rs.getString("date"),
            rs.getString("genres"),
            rs.getString("characters"),
            rs.getString("synopsis"),
            rs.getString("coverUrl")
        );
    }
}