package cn.hoob.rs.utils;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KerberosUtils {

	private static Logger log = LoggerFactory.getLogger(KerberosUtils.class);

	/**
	 * hdfs时候加载kerberos认证
	 * @return
	 * @throws Exception
	 */
	public static boolean hdfsIsKerberos() throws Exception {
		String coreSitePath = "/etc/hadoop/conf.cloudera.hdfs/core-site.xml";
		String hdfsSiteFile = FileUtils.readFile(coreSitePath);
		if (hdfsSiteFile == null) {
			log.warn("{} is not exist!", coreSitePath);
			return false;
		}
		XsiteXml hdfsSite = XmlUtils.xml2Object(hdfsSiteFile, XsiteXml.class);
		return containsPropertyKeyAndValue(hdfsSite, "hadoop.security.authentication", "kerberos");
	}

	/**
	 * hbase时候加载kerberos认证
	 * @return
	 * @throws Exception
	 */
    public static boolean hbaseIsKerberos() throws Exception {
		String hdfsSitePath = "/etc/hbase/conf.cloudera.hbase/hbase-site.xml";
		String hbaseSiteFile = FileUtils.readFile(hdfsSitePath);
		if (hbaseSiteFile == null) {
			log.warn("{} is not exist!", hdfsSitePath);
			return false;
		}
		XsiteXml hbaseSite = XmlUtils.xml2Object(hbaseSiteFile, XsiteXml.class);
		return containsPropertyKeyAndValue(hbaseSite, "hbase.security.authentication", "kerberos");
	}

	/**
	 * 判断是否含有某个属性与值
	 * @param xsiteXml
	 * @param key
	 * @param value
	 * @return
	 */
	private static boolean containsPropertyKeyAndValue(XsiteXml xsiteXml, String key, String value) {
		List<KerberosUtils.XsiteXml.XProperty> propertyList = xsiteXml.getPropertyList();
		for (KerberosUtils.XsiteXml.XProperty property : propertyList) {
			if (property.getName().equals(key) && property.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "configuration")
	public static class XsiteXml implements Serializable {
		private static final long serialVersionUID = 1L;
		@XmlElements(value = { @XmlElement(name = "property") })
		private List<XProperty> propertyList;

		public List<XProperty> getPropertyList() {
			return propertyList;
		}

		@XmlAccessorType(XmlAccessType.FIELD)
		private static class XProperty implements Serializable {
			private static final long serialVersionUID = 1L;
			@XmlElement(name = "name")
			private String name;
			@XmlElement(name = "value")
			private String value;

			public String getName() {
				return name;
			}

			public String getValue() {
				return value;
			}
		}
	}

/*	public static void main(String[] args) throws Exception {
		System.out.println(hbaseIsKerberos());
	}*/
}
