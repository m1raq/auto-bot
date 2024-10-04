package telegramBot.database.connection;

import lombok.Getter;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import telegramBot.database.entity.HelloEntity;

@Getter
public class HelloQueryInit implements QueryInit{

    public HelloQueryInit(){
        createCriteriaBuilder();
        createCriteriaQuery();
    }

    private JpaCriteriaQuery<HelloEntity> criteriaQuery;
    private HibernateCriteriaBuilder criteriaBuilder;

    @Override
    public void createCriteriaBuilder() {
        criteriaBuilder = ConnectToPostgreSQL.connection().getCriteriaBuilder();
    }

    @Override
    public void createCriteriaQuery() {
        criteriaQuery = criteriaBuilder.createQuery(HelloEntity.class);
    }
}
