package telegramBot.database.fetchers;

import org.hibernate.Transaction;
import org.hibernate.query.criteria.JpaRoot;
import telegramBot.database.connection.ConnectToPostgreSQL;
import telegramBot.database.connection.HelloQueryInit;
import telegramBot.database.dto.HelloDto;
import telegramBot.database.entity.HelloEntity;
import telegramBot.database.mapper.HelloMapper;



public class HelloQueries {

    private static final HelloQueryInit MANAGER = new HelloQueryInit();
    private static final JpaRoot<HelloEntity> ROOT = MANAGER.getCriteriaQuery().from(HelloEntity.class);


    public static HelloDto getHello(){
        return ConnectToPostgreSQL.connection()
                .createQuery(MANAGER.getCriteriaQuery()
                        .select(ROOT))
                .stream()
                .map(HelloMapper::mapToDto)
                .toList().stream().findFirst().get();
    }

    public static void edit(HelloDto helloDto){
        Transaction transaction = ConnectToPostgreSQL.connection().beginTransaction();
        ConnectToPostgreSQL.connection()
                .createQuery("delete from HelloEntity").executeUpdate();
        transaction.commit();
        transaction = ConnectToPostgreSQL.connection().beginTransaction();
        ConnectToPostgreSQL.connection().saveOrUpdate(HelloMapper.mapToEntity(helloDto));
        transaction.commit();
    }


}
