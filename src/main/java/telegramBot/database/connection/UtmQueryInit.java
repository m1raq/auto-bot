package telegramBot.database.connection;

import lombok.Getter;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import telegramBot.database.entity.UtmEntity;

@Getter
public class UtmQueryInit implements QueryInit {

    public UtmQueryInit(){
        createCriteriaBuilder();
        createCriteriaQuery();
    }

    private JpaCriteriaQuery<UtmEntity> criteriaQuery;
    private HibernateCriteriaBuilder criteriaBuilder;

    @Override
    public void createCriteriaBuilder(){
        criteriaBuilder = ConnectToPostgreSQL.connection().getCriteriaBuilder();
    }

    @Override
    public void createCriteriaQuery(){
        criteriaQuery = criteriaBuilder.createQuery(UtmEntity.class);
    }

}
