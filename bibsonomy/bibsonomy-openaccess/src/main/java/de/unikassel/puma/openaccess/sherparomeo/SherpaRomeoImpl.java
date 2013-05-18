/**
 *
 *  BibSonomy-OpenAccess - Check Open Access Policies for Publications
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.unikassel.puma.openaccess.sherparomeo;

import static org.bibsonomy.util.ValidationUtils.present;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import de.unikassel.puma.openaccess.sherparomeo.model.Condition;
import de.unikassel.puma.openaccess.sherparomeo.model.Publisher;
import de.unikassel.puma.openaccess.sherparomeo.model.Romeoapi;

/**
 * @version $Id: SherpaRomeoImpl.java,v 1.2 2011-04-21 08:30:45 rja Exp $
 * @author rja
 *
 */
public class SherpaRomeoImpl implements SherpaRomeoInterface {

    /*
     * TODO: working but ugly code.
     */

    private JAXBContext context;

    public SherpaRomeoImpl() {
        try {
            this.context = JAXBContext.newInstance(Condition.class.getPackage().getName());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPolicyForPublisher(String publisher, String qtype) {
        try {
            String url = "http://www.sherpa.ac.uk/romeo/api24.php?pub=" + URLEncoder.encode(publisher, "UTF-8");
            if (present(qtype))
                url += "&qtype=" + URLEncoder.encode(qtype, "UTF-8");

            return this.doRequest(new URL(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getPolicyForJournal(String jtitle, String qtype) {
        try {
            String url = "http://www.sherpa.ac.uk/romeo/api24.php?jtitle=" + URLEncoder.encode(jtitle, "UTF-8");
            if (present(qtype))
                url += "&qtype=" + URLEncoder.encode(qtype, "UTF-8");

            return this.doRequest(new URL(url));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sending request to sherparomeo.
     * 
     * FIXME: conditions for pre-/postprints missing!
     * 
     * 
     * @param url
     * @return
     */
    @SuppressWarnings("unchecked")
    private String doRequest(final URL url) {
        try {
            final Unmarshaller unmarshaller = this.context.createUnmarshaller();
            final Romeoapi rp = (Romeoapi) unmarshaller.unmarshal(url);

            // log.debug("Checking Open Access: \t" + url);

            final List<Publisher> publishers = rp.getPublishers().getPublisher();
            final JSONObject result = new JSONObject();
            final JSONArray publishersJson = new JSONArray();
            for (final Publisher publisher : publishers) {
                final JSONObject publisherJson = new JSONObject();
                publisherJson.put("name", publisher.getName());
                publisherJson.put("colour", publisher.getRomeocolour());
                
                publisherJson.put("conditions", renderConditions(publisher.getConditions().getCondition()));
                publishersJson.add(publisherJson);
            }
            result.put("publishers", publishersJson);

            return result.toString();

        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

	@SuppressWarnings("unchecked")
	private JSONArray renderConditions(final List<Condition> conditions) {
		final JSONArray conditionsJson = new JSONArray();
		for (final Condition condition : conditions)
		    conditionsJson.add(condition.getvalue());
		return conditionsJson;
	}
}
