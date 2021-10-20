package ec.project.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface DataRepository extends JpaRepository<Data, Long>{
   
	List<Data> findAll();

}