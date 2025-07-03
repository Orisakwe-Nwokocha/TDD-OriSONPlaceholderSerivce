package dev.orisha.orison.post.service;

import dev.orisha.orison.post.data.model.Address;
import dev.orisha.orison.post.data.model.Company;
import dev.orisha.orison.post.data.model.Geo;
import dev.orisha.orison.post.data.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setWebsite(rs.getString("website"));
        user.setVersion(rs.getLong("version"));

        Address address = new Address();
        address.setStreet(rs.getString("address_street"));
        address.setSuite(rs.getString("address_suite"));
        address.setCity(rs.getString("address_city"));
        address.setZipcode(rs.getString("address_zipcode"));

        Geo geo = new Geo();
        geo.setLat(rs.getString("address_geo_lat"));
        geo.setLng(rs.getString("address_geo_lng"));
        address.setGeo(geo);

        user.setAddress(address);

        Company company = new Company();
        company.setName(rs.getString("company_name"));
        company.setCatchPhrase(rs.getString("company_catch_phrase"));
        company.setBs(rs.getString("company_bs"));

        user.setCompany(company);

        return user;
    };


    public Page<User> findAll(Pageable pageable) {
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Long.class);
//        total = null;

        if (total == null || total == 0L) {
            return new PageImpl<>(List.of());
        }

        String sql = String.format(
                "SELECT * FROM users ORDER BY %s LIMIT %d OFFSET %d",
                getSort(pageable),
                pageable.getPageSize(),
                pageable.getOffset()
        );

        List<User> content = jdbcTemplate.query(sql, userRowMapper);
        return new PageImpl<>(content, pageable, total);
    }

    private String getSort(Pageable pageable) {
        if (pageable.getSort().isUnsorted()) {
            return "id ASC";
        }
        return pageable.getSort().stream()
                .map(order -> order.getProperty().replaceAll("[\"\\[\\]]", "").strip() + " " + order.getDirection().name())
                .collect(Collectors.joining(", "));
    }

}
