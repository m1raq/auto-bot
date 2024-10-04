package telegramBot.database.fetchers;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.JpaRoot;
import telegramBot.database.connection.ConnectToPostgreSQL;
import telegramBot.database.connection.UserQueryInit;
import telegramBot.database.dto.UserDto;
import telegramBot.database.entity.UserEntity;
import telegramBot.database.mapper.UserMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class UserQueries {

    private static final UserQueryInit MANAGER = new UserQueryInit();
    private static final JpaRoot<UserEntity> ROOT = MANAGER.getCriteriaQuery().from(UserEntity.class);

    public static UserDto getByUsername(String name){
        UserQueryInit manager = new UserQueryInit();
        JpaRoot<UserEntity> root = manager.getCriteriaQuery().from(UserEntity.class);

        return ConnectToPostgreSQL.connection()
                .createQuery(manager.getCriteriaQuery()
                        .select(root)
                        .where(manager.getCriteriaBuilder().equal(root.get("username"), name)))
                .stream()
                .map(UserMapper::mapToDto)
                .filter(userDto -> Objects.equals(userDto.getUsername(), name))
                .findFirst()
                .orElse(null);

    }

    public static List<UserDto> getAllManager(){
        return getAll()
                .stream()
                .filter(UserDto::getIsManager)
                .collect(Collectors.toList());
    }

    public static List<UserDto> getAll(){

        return ConnectToPostgreSQL.connection()
                .createQuery(MANAGER.getCriteriaQuery()
                        .select(ROOT))
                .stream()
                .map(UserMapper::mapToDto)
                .collect(Collectors.toList());

    }

    public static void save(UserEntity user){
        Transaction transaction = ConnectToPostgreSQL.connection().beginTransaction();
        ConnectToPostgreSQL.connection().save(user);
        transaction.commit();
    }

    public static void update(UserDto userDto){
        Transaction transaction = ConnectToPostgreSQL.connection().beginTransaction();
        ConnectToPostgreSQL.connection().merge(UserMapper.mapToEntity(userDto));
        transaction.commit();
    }


    public static void deleteAll(){
        Transaction transaction = ConnectToPostgreSQL.connection().beginTransaction();
        ConnectToPostgreSQL.connection()
                .createQuery("delete from UserEntity").executeUpdate();
        transaction.commit();
    }



}
