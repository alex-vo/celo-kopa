package lv.celokopa.config.mysql;

import org.hibernate.dialect.MySQL5InnoDBDialect;

/**
 * Created by alex on 16.4.11.
 */
public class CustomMySQLDialect extends MySQL5InnoDBDialect {
    @Override
    public String getTableTypeString() {
        return " DEFAULT CHARSET=utf8 ENGINE = MyISAM";
    }
}
