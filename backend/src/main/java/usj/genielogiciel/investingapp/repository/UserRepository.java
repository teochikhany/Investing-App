package usj.genielogiciel.investingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usj.genielogiciel.investingapp.model.AppUser;


// JpaRepository extends PagingAndSortingRepository extends CrudRepository
@Repository
public interface UserRepository extends JpaRepository<AppUser, Long>
{
    AppUser findByUsername(String username);
}
