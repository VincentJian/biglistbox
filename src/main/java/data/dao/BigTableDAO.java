package data.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.zkoss.util.logging.Log;
import org.zkoss.zkplus.hibernate.HibernateUtil;

import data.pojo.BigTable;

public class BigTableDAO {
	private static final Log log = Log.lookup(BigTableDAO.class);
	
	public boolean saveOrUpdate(BigTable bigTable) {
		boolean result = false;
		Session session = HibernateUtil.currentSession();
		try {
			session.beginTransaction();
			session.saveOrUpdate(bigTable);
			session.getTransaction().commit();
			log.info("create or update succeed");
			result = true;
		} catch (HibernateException he) {
			log.error("create or update failed", he);
		}
		return result;
	}
	
	public BigTable findById(int id) {
		Session session = HibernateUtil.currentSession();
		BigTable bigTable = null;
		try {
			session.beginTransaction();
			bigTable = (BigTable) session.get(BigTable.class, id);
			session.getTransaction().commit();
			if (bigTable == null) {
				log.info("get successful, no instance found");
			} else {
				log.info("get successful, instance found");
			}
		} catch (HibernateException he) {
			log.error("get failed", he);
		}
		return bigTable;
	}
	
	@SuppressWarnings("unchecked")
	public List<BigTable> findBetweenIds(int startId, int endId, boolean sortDir) {
		Session session = HibernateUtil.currentSession();
		List<BigTable> list = null;
		try {
			session.beginTransaction();
			String query = "SELECT b FROM BigTable b WHERE b.id BETWEEN :start AND :end ORDER BY b.id "
					+ (sortDir ? "asc" : "desc");
			Query q = session.createQuery(query);
			q.setInteger("start", startId);
			q.setInteger("end", endId);
			list = q.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			log.error("get failed", he);
		}
		return list;
	}
}
