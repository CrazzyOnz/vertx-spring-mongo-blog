package com.cisco.cmad.projects.blog.dao;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cisco.cmad.projects.blog.config.SpringConfigurationTest;
import com.cisco.cmad.projects.blog.dto.Post;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringConfigurationTest.class)
public class BlogDAOImplTest {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private BlogDAO blogDao;
	
	@Test
	public void createBlogTest() {
		Post blog = new Post("My First blog","Some content","admin");
		Assert.assertTrue(blogDao.createBlog(blog));
		Post blog2 = blogDao.getBlogById(blog.get_id()).get();
		Assert.assertEquals(blog.get_id(), blog2.get_id());
		Assert.assertEquals(blog.getTitle(), blog2.getTitle());
		Assert.assertTrue(blogDao.deleteBlogById(blog.get_id()));
		Assert.assertFalse(blogDao.getBlogById(blog.get_id()).isPresent());
	}
	
	@Test
	public void setFavouriteTest() {
		Post blog = new Post("My First blog","Some content","admin");
		blog.setFavorite(false);
		blogDao.createBlog(blog);
		blogDao.setFavourite(blog.get_id(), true);
		Post blog2 = blogDao.getBlogById(blog.get_id()).get();
		Assert.assertTrue(blog2.isFavorite());
		blogDao.deleteBlogById(blog.get_id());
		Assert.assertTrue(blogDao.deleteBlogById(blog.get_id()));
		Assert.assertFalse(blogDao.getBlogById(blog.get_id()).isPresent());
	}
	
	@Test
	public void getFavouriteBlogsByIdTest() {
		String userId = UUID.randomUUID().toString();
		Post blog = new Post("My First favorite blog","Some content",userId);
		blogDao.createBlog(blog);
		blogDao.setFavourite(blog.get_id(), true);
		Assert.assertEquals(1, blogDao.getFavouriteBlogs(userId).size());
		Assert.assertEquals(blog.getTitle(), blogDao.getFavouriteBlogs(userId).get(0).getTitle());
		
		Post blog2 = new Post("My Second favorite blog","Some content",userId);
		blogDao.createBlog(blog2);
		blogDao.setFavourite(blog2.get_id(), true);
		Assert.assertEquals(2, blogDao.getFavouriteBlogs(userId).size());
		
		blogDao.deleteBlogById(blog.get_id());
		Assert.assertTrue(blogDao.deleteBlogById(blog.get_id()));
		Assert.assertFalse(blogDao.getBlogById(blog.get_id()).isPresent());
		
		blogDao.deleteBlogById(blog2.get_id());
		Assert.assertTrue(blogDao.deleteBlogById(blog2.get_id()));
		Assert.assertFalse(blogDao.getBlogById(blog2.get_id()).isPresent());
	}
	
	@Test
	public void getBlogByTitleTest() {
		String userId = "test";
		Post blog = new Post(UUID.randomUUID().toString(),"Some content",userId);
		blogDao.createBlog(blog);
		List<Post> blogsByTitle = blogDao.getBlogsByTitle(blog.getTitle().substring(blog.getTitle().indexOf("-"), blog.getTitle().length()),userId);
		Assert.assertEquals(1, blogsByTitle.size());
		
		Post blog2 = new Post(UUID.randomUUID().toString()+blog.getTitle(),"Some content","test");
		blogDao.createBlog(blog2);
		blogsByTitle = blogDao.getBlogsByTitle(blog.getTitle().substring(blog.getTitle().indexOf("-"), blog.getTitle().length()),userId);
		Assert.assertEquals(2, blogsByTitle.size());
		
		blogDao.deleteBlogById(blog.get_id());
		Assert.assertTrue(blogDao.deleteBlogById(blog.get_id()));
		Assert.assertFalse(blogDao.getBlogById(blog.get_id()).isPresent());
		
		blogDao.deleteBlogById(blog2.get_id());
		Assert.assertTrue(blogDao.deleteBlogById(blog2.get_id()));
		Assert.assertFalse(blogDao.getBlogById(blog2.get_id()).isPresent());
	}
	
	@Test
	public void getAllBlogs() {
		List<Post> allBlogs = blogDao.getAllBlogs();
		logger.info("blogs : "+allBlogs);
	}
	
}
