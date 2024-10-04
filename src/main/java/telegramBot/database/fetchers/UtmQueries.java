package telegramBot.database.fetchers;

import org.hibernate.Transaction;
import org.hibernate.query.criteria.JpaRoot;
import telegramBot.database.connection.ConnectToPostgreSQL;
import telegramBot.database.connection.UtmQueryInit;
import telegramBot.database.dto.UtmDto;
import telegramBot.database.entity.UtmEntity;
import telegramBot.database.mapper.UtmMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UtmQueries {

    private static final UtmQueryInit MANAGER = new UtmQueryInit();
    private static final JpaRoot<UtmEntity> ROOT = MANAGER.getCriteriaQuery().from(UtmEntity.class);

    public static List<UtmDto> getAll(){
        return ConnectToPostgreSQL.connection()
                .createQuery(MANAGER.getCriteriaQuery()
                        .select(ROOT))
                .stream()
                .map(UtmMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public static void save(UtmEntity utmEntity){
        Transaction transaction = ConnectToPostgreSQL.connection().beginTransaction();
        ConnectToPostgreSQL.connection().save(utmEntity);
        transaction.commit();
    }

    public static void deleteAll(){
        Transaction transaction = ConnectToPostgreSQL.connection().beginTransaction();
        ConnectToPostgreSQL.connection()
                .createQuery("delete from UtmEntity").executeUpdate();
        transaction.commit();
    }

    public static void deleteByValue(String value){
        Transaction transaction = ConnectToPostgreSQL.connection().beginTransaction();
        ConnectToPostgreSQL.connection()
                .createQuery("delete from UtmEntity where utmValue = :utmValue")
                .setParameter("utmValue", value)
                .executeUpdate();
        transaction.commit();
    }

}
