/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 *    InstanceQuery.java
 *    Copyright (C) 1999-2012 University of Waikato, Hamilton, New Zealand
 *
 */

package ec.project.weka;

import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.RevisionUtils;
import weka.core.SparseInstance;
import weka.core.Utils;

/**
 * Convert the results of a database query into instances. The jdbc driver and
 * database to be used default to "jdbc.idbDriver" and
 * "jdbc:idb=experiments.prp". These may be changed by creating a java
 * properties file called DatabaseUtils.props in user.home or the current
 * directory. eg:
 * <p>
 * 
 * <code><pre>
 * jdbcDriver=jdbc.idbDriver
 * jdbcURL=jdbc:idb=experiments.prp
 * </pre></code>
 * <p>
 * 
 * Command line use just outputs the instances to System.out.
 * <p/>
 * 
 * <!-- options-start --> Valid options are:
 * <p/>
 * 
 * <pre>
 * -Q &lt;query&gt;
 *  SQL query to execute.
 * </pre>
 * 
 * <pre>
 * -S
 *  Return sparse rather than normal instances.
 * </pre>
 * 
 * <pre>
 * -U &lt;username&gt;
 *  The username to use for connecting.
 * </pre>
 * 
 * <pre>
 * -P &lt;password&gt;
 *  The password to use for connecting.
 * </pre>
 * 
 * <pre>
 * -D
 *  Enables debug output.
 * </pre>
 * 
 * <!-- options-end -->
 * 
 * @author Len Trigg (trigg@cs.waikato.ac.nz)
 * @version $Revision$
 */
public class InstanceQuery implements OptionHandler,
  InstanceQueryAdapter {
	
	  /* Type mapping used for reading experiment results */
	  /** Type mapping for STRING used for reading experiment results. */
	  public static final int STRING = 0;
	  /** Type mapping for BOOL used for reading experiment results. */
	  public static final int BOOL = 1;
	  /** Type mapping for DOUBLE used for reading experiment results. */
	  public static final int DOUBLE = 2;
	  /** Type mapping for BYTE used for reading experiment results. */
	  public static final int BYTE = 3;
	  /** Type mapping for SHORT used for reading experiment results. */
	  public static final int SHORT = 4;
	  /** Type mapping for INTEGER used for reading experiment results. */
	  public static final int INTEGER = 5;
	  /** Type mapping for LONG used for reading experiment results. */
	  public static final int LONG = 6;
	  /** Type mapping for FLOAT used for reading experiment results. */
	  public static final int FLOAT = 7;
	  /** Type mapping for DATE used for reading experiment results. */
	  public static final int DATE = 8;
	  /** Type mapping for TEXT used for reading, e.g., text blobs. */
	  public static final int TEXT = 9;
	  /** Type mapping for TIME used for reading TIME columns. */
	  public static final int TIME = 10;
	  /** Type mapping for TIMESTAMP used for reading java.sql.Timestamp columns */
	  public static final int TIMESTAMP = 11;

  /** for serialization */
  static final long serialVersionUID = 718158370917782584L;

  /** Determines whether sparse data is created */
  protected boolean m_CreateSparseData = false;

  /** Query to execute */
  protected String m_Query = "SELECT * from ?";

  /** the custom props file to use instead of default one. */
  protected File m_CustomPropsFile = null;

  /**
   * Sets up the database drivers
   * 
   * @throws Exception if an error occurs
   */
  public InstanceQuery() throws Exception {

    super();
  }

  public static Instances retrieveInstances(InstanceQueryAdapter adapter,
    ResultSet rs) throws Exception {
    if (adapter.getDebug()) {
      System.err.println("Getting metadata...");
    }
    ResultSetMetaData md = rs.getMetaData();
    if (adapter.getDebug()) {
      System.err.println("Completed getting metadata...");
    }

    // Determine structure of the instances
    int numAttributes = md.getColumnCount();
    int[] attributeTypes = new int[numAttributes];
    @SuppressWarnings("unchecked")
    Hashtable<String, Double>[] nominalIndexes = new Hashtable[numAttributes];
    @SuppressWarnings("unchecked")
    ArrayList<String>[] nominalStrings = new ArrayList[numAttributes];
    for (int i = 1; i <= numAttributes; i++) {
      /*
       * switch (md.getColumnType(i)) { case Types.CHAR: case Types.VARCHAR:
       * case Types.LONGVARCHAR: case Types.BINARY: case Types.VARBINARY: case
       * Types.LONGVARBINARY:
       */

      switch (adapter.translateDBColumnType(md.getColumnTypeName(i))) {

      case STRING:
        // System.err.println("String --> nominal");
        attributeTypes[i - 1] = Attribute.NOMINAL;
        nominalIndexes[i - 1] = new Hashtable<String, Double>();
        nominalStrings[i - 1] = new ArrayList<String>();
        break;
      case TEXT:
        // System.err.println("Text --> string");
        attributeTypes[i - 1] = Attribute.STRING;
        nominalIndexes[i - 1] = new Hashtable<String, Double>();
        nominalStrings[i - 1] = new ArrayList<String>();
        break;
      case BOOL:
        // System.err.println("boolean --> nominal");
        attributeTypes[i - 1] = Attribute.NOMINAL;
        nominalIndexes[i - 1] = new Hashtable<String, Double>();
        nominalIndexes[i - 1].put("false", new Double(0));
        nominalIndexes[i - 1].put("true", new Double(1));
        nominalStrings[i - 1] = new ArrayList<String>();
        nominalStrings[i - 1].add("false");
        nominalStrings[i - 1].add("true");
        break;
      case DOUBLE:
        // System.err.println("BigDecimal --> numeric");
        attributeTypes[i - 1] = Attribute.NUMERIC;
        break;
      case BYTE:
        // System.err.println("byte --> numeric");
        attributeTypes[i - 1] = Attribute.NUMERIC;
        break;
      case SHORT:
        // System.err.println("short --> numeric");
        attributeTypes[i - 1] = Attribute.NUMERIC;
        break;
      case INTEGER:
        // System.err.println("int --> numeric");
        attributeTypes[i - 1] = Attribute.NUMERIC;
        break;
      case LONG:
        // System.err.println("long --> numeric");
        attributeTypes[i - 1] = Attribute.NUMERIC;
        break;
      case FLOAT:
        // System.err.println("float --> numeric");
        attributeTypes[i - 1] = Attribute.NUMERIC;
        break;
      case DATE:
        attributeTypes[i - 1] = Attribute.DATE;
        break;
      case TIME:
        attributeTypes[i - 1] = Attribute.DATE;
        break;
      case TIMESTAMP:
        attributeTypes[i - 1] = Attribute.DATE;
        break;
      default:
        // System.err.println("Unknown column type");
        attributeTypes[i - 1] = Attribute.STRING;
      }
    }

    // For sqlite
    // cache column names because the last while(rs.next()) { iteration for
    // the tuples below will close the md object:
    Vector<String> columnNames = new Vector<String>();
    for (int i = 0; i < numAttributes; i++) {
      columnNames.add(md.getColumnLabel(i + 1));
    }

    // Step through the tuples
    if (adapter.getDebug()) {
      System.err.println("Creating instances...");
    }
    ArrayList<Instance> instances = new ArrayList<Instance>();
    int rowCount = 0;
    while (rs.next()) {
      if (rowCount % 100 == 0) {
        if (adapter.getDebug()) {
          System.err.print("read " + rowCount + " instances \r");
          System.err.flush();
        }
      }
      double[] vals = new double[numAttributes];
      for (int i = 1; i <= numAttributes; i++) {
        /*
         * switch (md.getColumnType(i)) { case Types.CHAR: case Types.VARCHAR:
         * case Types.LONGVARCHAR: case Types.BINARY: case Types.VARBINARY: case
         * Types.LONGVARBINARY:
         */
        switch (adapter.translateDBColumnType(md.getColumnTypeName(i))) {
        case STRING:
          String str = rs.getString(i);

          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            Double index = nominalIndexes[i - 1].get(str);
            if (index == null) {
              index = new Double(nominalStrings[i - 1].size());
              nominalIndexes[i - 1].put(str, index);
              nominalStrings[i - 1].add(str);
            }
            vals[i - 1] = index.doubleValue();
          }
          break;
        case TEXT:
          String txt = rs.getString(i);

          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            Double index = nominalIndexes[i - 1].get(txt);
            if (index == null) {

              // Need to add one because first value in
              // string attribute is dummy value.
              index = new Double(nominalStrings[i - 1].size()) + 1;
              nominalIndexes[i - 1].put(txt, index);
              nominalStrings[i - 1].add(txt);
            }
            vals[i - 1] = index.doubleValue();
          }
          break;
        case BOOL:
          boolean boo = rs.getBoolean(i);
          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            vals[i - 1] = (boo ? 1.0 : 0.0);
          }
          break;
        case DOUBLE:
          // BigDecimal bd = rs.getBigDecimal(i, 4);
          double dd = rs.getDouble(i);
          // Use the column precision instead of 4?
          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            // newInst.setValue(i - 1, bd.doubleValue());
            vals[i - 1] = dd;
          }
          break;
        case BYTE:
          byte by = rs.getByte(i);
          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            vals[i - 1] = by;
          }
          break;
        case SHORT:
          short sh = rs.getShort(i);
          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            vals[i - 1] = sh;
          }
          break;
        case INTEGER:
          int in = rs.getInt(i);
          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            vals[i - 1] = in;
          }
          break;
        case LONG:
          long lo = rs.getLong(i);
          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            vals[i - 1] = lo;
          }
          break;
        case FLOAT:
          float fl = rs.getFloat(i);
          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            vals[i - 1] = fl;
          }
          break;
        case DATE:
          Date date = rs.getDate(i);
          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            // TODO: Do a value check here.
            vals[i - 1] = date.getTime();
          }
          break;
        case TIME:
          Time time = rs.getTime(i);
          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            // TODO: Do a value check here.
            vals[i - 1] = time.getTime();
          }
          break;
        case TIMESTAMP:
          Timestamp ts = rs.getTimestamp(i);
          if (rs.wasNull()) {
            vals[i - 1] = Utils.missingValue();
          } else {
            vals[i - 1] = ts.getTime();
          }
          break;
        default:
          vals[i - 1] = Utils.missingValue();
        }
      }
      Instance newInst;
      if (adapter.getSparseData()) {
        newInst = new SparseInstance(1.0, vals);
      } else {
        newInst = new DenseInstance(1.0, vals);
      }
      instances.add(newInst);
      rowCount++;
    }
    // disconnectFromDatabase(); (perhaps other queries might be made)

    // Create the header and add the instances to the dataset
    if (adapter.getDebug()) {
      System.err.println("Creating header...");
    }
    ArrayList<Attribute> attribInfo = new ArrayList<Attribute>();
    for (int i = 0; i < numAttributes; i++) {
      /* Fix for databases that uppercase column names */
      // String attribName = attributeCaseFix(md.getColumnName(i + 1));
      String attribName = adapter.attributeCaseFix(columnNames.get(i));
      switch (attributeTypes[i]) {
      case Attribute.NOMINAL:
        attribInfo.add(new Attribute(attribName, nominalStrings[i]));
        break;
      case Attribute.NUMERIC:
        attribInfo.add(new Attribute(attribName));
        break;
      case Attribute.STRING:
        Attribute att = new Attribute(attribName, (ArrayList<String>) null);
        attribInfo.add(att);
        for (int n = 0; n < nominalStrings[i].size(); n++) {
          att.addStringValue(nominalStrings[i].get(n));
        }
        break;
      case Attribute.DATE:
        attribInfo.add(new Attribute(attribName, (String) null));
        break;
      default:
        throw new Exception("Unknown attribute type");
      }
    }
    Instances result = new Instances("QueryResult", attribInfo,
      instances.size());
    for (int i = 0; i < instances.size(); i++) {
      result.add(instances.get(i));
    }

    return result;
  }

@Override
public String attributeCaseFix(String columnName) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public boolean getDebug() {
	// TODO Auto-generated method stub
	return false;
}

@Override
public boolean getSparseData() {
	// TODO Auto-generated method stub
	return false;
}

@Override
/**
 * translates the column data type string to an integer value that indicates
 * which data type / get()-Method to use in order to retrieve values from the
 * database (see DatabaseUtils.Properties, InstanceQuery()). Blanks in the
 * type are replaced with underscores "_", since Java property names can't
 * contain blanks.
 * 
 * @param type the column type as retrieved with
 *          java.sql.MetaData.getColumnTypeName(int)
 * @return an integer value that indicates which data type / get()-Method to
 *         use in order to retrieve values from the
 */
public int translateDBColumnType(String type) {

    switch (type) {
    case "CHAR": return 0;
    case "TEXT": return 0;
    case "VARCHAR": return 0;
    case "LONGVARCHAR": return 9;
    case "BINARY": return 0;
    case "VARBINARY": return 0;
    case "LONGVARBINARY": return 9;
    case "BIT": return 1;
    case "NUMERIC": return 2;
    case "DECIMAL": return 2;
    case "FLOAT": return 2;
    case "DOUBLE": return 2;
    case "TINYINT": return 3;
    case "SMALLINT": return 4;
    case "SHORT": return 5;
    case "INTEGER": return 5;
    case "BIGINT": return 6;
    case "LONG": return 6;
    case "REAL": return 7;
    case "DATE": return 8;
    case "TIME": return 10;
    case "TIMESTAMP": return 11;
    default: return -1;
    
    }
}

@Override
public Enumeration<Option> listOptions() {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void setOptions(String[] options) throws Exception {
	// TODO Auto-generated method stub
	
}

@Override
public String[] getOptions() {
	// TODO Auto-generated method stub
	return null;
}

}