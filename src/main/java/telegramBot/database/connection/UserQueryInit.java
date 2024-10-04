package telegramBot.database.connection;

import lombok.Getter;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import telegramBot.database.entity.UserEntity;

@Getter
public class UserQueryInit implements QueryInit {
    public UserQueryInit(){
        createCriteriaBuilder();
        createCriteriaQuery();
    }

    private JpaCriteriaQuery<UserEntity> criteriaQuery;
    private HibernateCriteriaBuilder criteriaBuilder;

    @Override
    public void createCriteriaBuilder(){
        criteriaBuilder = ConnectToPostgreSQL.connection().getCriteriaBuilder();
    }

    @Override
    public void createCriteriaQuery(){
        criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
    }

}
