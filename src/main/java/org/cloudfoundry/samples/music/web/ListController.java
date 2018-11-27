package org.cloudfoundry.samples.music.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.cloudfoundry.samples.music.domain.Album;
import org.cloudfoundry.samples.music.domain.RandomIdGenerator;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/list")
public class ListController {
	public static final String ALBUMS_KEY = "albums";
	
	@Resource(name="redisTemplate")
	private HashOperations<String, String, Album> hashOperations;
	
	private final RandomIdGenerator idGenerator;
	
	public ListController() {
		this.idGenerator = new RandomIdGenerator();
	}
	
	public Album redisOneAdd(Album album) {
		if (album.getId() == null) {
	        album.setId(idGenerator.generateId());
	    }

		hashOperations.put(ALBUMS_KEY,album.getId(), album);
   
		return album;
	}
	
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public void send(@RequestBody @Valid Iterable<Album> albums) {
    	System.out.println("list/send");
    	List<Album> result = new ArrayList<>();
    	
    	if (hashOperations.values(ALBUMS_KEY).size() > 0) {
    		Set<String> ids = hashOperations.keys(ALBUMS_KEY);
            for (String id : ids) {
                hashOperations.delete(ALBUMS_KEY, id);
            }
            System.out.println("list empty!!");
		}
    	  
    	for (Album entity : albums) {

    		redisOneAdd(entity);
    		result.add(entity);
    	}
    	
    	System.out.println("****completed!!!!!!!!!!!!");
    }
    
//**************get
  @RequestMapping( value = "/get", method = RequestMethod.GET)
  public Iterable<Album> findAll() {

	  System.out.println("list/get");

	  return hashOperations.values(ALBUMS_KEY);
  }

}
