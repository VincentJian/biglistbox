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
			session.saveOrUpdate(bigTable);
			log.info("create or update succeed");
			result = true;
		} catch (HibernateException he) {
			log.error("create or update failed", he);
		}
		return result;
	}
	
	public boolean delete(BigTable bigTable) {
		boolean result = false;
		Session session = HibernateUtil.currentSession();
		try {
			session.delete(bigTable);
			log.info("delete succeed");
			result = true;
		} catch (HibernateException he) {
			log.error("delete failed", he);
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
	public List<BigTable> findBetweenIds(int startId, int endId) {
		Session session = HibernateUtil.currentSession();
		List<BigTable> list = null;
		try {
			session.beginTransaction();
			Query q = session.createQuery("select b from BigTable b where b.id between :start and :end");
			q.setInteger("start", startId);
			q.setInteger("end", endId);
			list = q.list();
			session.getTransaction().commit();
		} catch (HibernateException he) {
			log.error("get failed", he);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<BigTable> findAll() {
		Session session = HibernateUtil.currentSession();
		List<BigTable> list = null;
		try {
			list = session.createQuery("from BigTable").list();
			log.info("find all successful, result size: " + list.size());
		} catch (HibernateException he) {
			log.error("get failed", he);
		}
		return list;
	}
}
