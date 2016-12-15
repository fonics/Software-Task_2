import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.Integer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)

public class U_test {
	public Product n;
	int id=1234;
	DAOImpl new_DAO = new DAOImpl();
	@Mock 
	Connection conn;
	@Mock
	PreparedStatement psmt;
	@InjectMocks
	DAOImpl newDAO = new DAOImpl();
	
    @Before
    public void setup(){
    	n = new Product(id); 
    }
	@Test
	public void testproductConstructor()
	{
		Assert.assertEquals(1234,n.getId());
		}
   
	@Test
	public void test_type_setter()
	{
		n.setType("toy");
		Assert.assertEquals("toy",n.getType());
	}
	@Test
	public void test_manfacturer_setter()
	{
		n.setManufacturer("Lego");
		Assert.assertEquals("Lego",n.getManufacturer());
	}
	
	@Test
	public void test_productiondate_setter()
	{
		n.setProductionDate("9-dec-2016");
		Assert.assertEquals("9-dec-2016",n.getProductionDate());
	}
	
	@Test
	public void test_expirydate_setter()
	{
		n.setExpiryDate("14-dec-2016");
		Assert.assertEquals("14-dec-2016",n.getExpiryDate());
	}
	
	
	
	@Test
	public void HappyTest1() throws SQLException, DAOException
	{
		when(conn.prepareStatement(anyString())).thenReturn(psmt);
		when(psmt.executeUpdate()).thenReturn(1);
		Product p = new Product(12);
		newDAO.insertProduct(p);
		}
	

	@Test (expected = DAOException.class)
	public void ExceptionCase() throws SQLException, DAOException{
		when(conn.prepareStatement(anyString())).thenReturn(psmt);
		when(psmt.executeUpdate()).thenThrow(new SQLException());
		Product p = new Product(35);
		newDAO.insertProduct(p);	
	}	
	@Test
	public void HappyTest2() throws SQLException, DAOException{
		when(conn.prepareStatement(anyString())).thenReturn(psmt);
		when(psmt.executeUpdate()).thenReturn(1);
        Product product1 = new Product(11);
		product1.setType("toy");
		product1.setManufacturer("lego");
		product1.setProductionDate("2015-12-2");
		product1.setExpiryDate("2016-12-1");	
		newDAO.updateProduct(product1);
		verify(psmt, times(1)).executeUpdate();
		ArgumentCaptor<Integer> stringcaptor = ArgumentCaptor.forClass(Integer.class);
		verify(psmt, times(1)).setInt(anyInt(), stringcaptor.capture());
		Assert.assertTrue(stringcaptor.getAllValues().get(0).equals(11));	
		ArgumentCaptor<String> Stringcaptor = ArgumentCaptor.forClass(String.class);
		verify(psmt, times(4)).setString(anyInt(), Stringcaptor.capture());	
		Assert.assertTrue(Stringcaptor.getAllValues().get(0).equals("toy"));
		Assert.assertTrue(Stringcaptor.getAllValues().get(1).equals("lego"));
	}

    @Test
     public void integration_test() throws Exception ,DAOException{
	    DAOImpl newtest = new DAOImpl();
        Product testprod = new Product(123);
        testprod.setType("orange juice");
        testprod.setManufacturer("lamar");
        testprod.setProductionDate("2015-2-12");
        testprod.setExpiryDate("2015-8-11");
        newtest.insertProduct(testprod);
        Assert.assertEquals(123,testprod.getId());
        
        newtest.getProduct(123);
        Assert.assertEquals(123,testprod.getId());
        Assert.assertEquals("orange juice",testprod.getType());
        Assert.assertEquals("lamar",testprod.getManufacturer());
        Assert.assertEquals("2015-2-12",testprod.getProductionDate());
        Assert.assertEquals("2015-8-11",testprod.getExpiryDate());
        
        newtest.deleteProduct(123);
        Assert.assertTrue(newtest.getProduct(1) == null);
}

	
}
