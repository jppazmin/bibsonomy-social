/**
 *
 *  BibSonomy-Scraper - Web page scrapers returning BibTeX for BibSonomy.
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

/*
 * Created on 05.09.2006
 */
package org.bibsonomy.scraper;

import org.bibsonomy.scraper.url.kde.aanda.AandAScraper;
import org.bibsonomy.scraper.url.kde.acl.AclScraper;
import org.bibsonomy.scraper.url.kde.acm.ACMBasicScraper;
import org.bibsonomy.scraper.url.kde.acs.ACSScraper;
import org.bibsonomy.scraper.url.kde.agu.AGUScraper;
import org.bibsonomy.scraper.url.kde.aip.AipScitationScraper;
import org.bibsonomy.scraper.url.kde.amazon.AmazonScraper;
import org.bibsonomy.scraper.url.kde.ams.AmsScraper;
import org.bibsonomy.scraper.url.kde.annualreviews.AnnualreviewsScraper;
import org.bibsonomy.scraper.url.kde.anthrosource.AnthroSourceScraper;
import org.bibsonomy.scraper.url.kde.arxiv.ArxivScraper;
import org.bibsonomy.scraper.url.kde.bibsonomy.BibSonomyScraper;
import org.bibsonomy.scraper.url.kde.biomed.BioMedCentralScraper;
import org.bibsonomy.scraper.url.kde.blackwell.BlackwellSynergyScraper;
import org.bibsonomy.scraper.url.kde.bmj.BMJScraper;
import org.bibsonomy.scraper.url.kde.cambridge.CambridgeScraper;
import org.bibsonomy.scraper.url.kde.casesjournal.CasesJournalScraper;
import org.bibsonomy.scraper.url.kde.cell.CellScraper;
import org.bibsonomy.scraper.url.kde.citebase.CiteBaseScraper;
import org.bibsonomy.scraper.url.kde.citeseer.CiteseerxScraper;
import org.bibsonomy.scraper.url.kde.citeulike.CiteulikeScraper;
import org.bibsonomy.scraper.url.kde.dblp.DBLPScraper;
import org.bibsonomy.scraper.url.kde.dlib.DLibScraper;
import org.bibsonomy.scraper.url.kde.editlib.EditLibScraper;
import org.bibsonomy.scraper.url.kde.elsevierhealth.ElsevierhealthScraper;
import org.bibsonomy.scraper.url.kde.eric.EricScraper;
import org.bibsonomy.scraper.url.kde.googlescholar.GoogleScholarScraper;
import org.bibsonomy.scraper.url.kde.ieee.IEEEComputerSocietyScraper;
import org.bibsonomy.scraper.url.kde.ieee.IEEEXploreScraper;
import org.bibsonomy.scraper.url.kde.informaworld.InformaWorldScraper;
import org.bibsonomy.scraper.url.kde.ingenta.IngentaconnectScraper;
import org.bibsonomy.scraper.url.kde.iop.IOPScraper;
import org.bibsonomy.scraper.url.kde.isi.IsiScraper;
import org.bibsonomy.scraper.url.kde.iucr.IucrScraper;
import org.bibsonomy.scraper.url.kde.iwap.IWAPonlineScraper;
import org.bibsonomy.scraper.url.kde.jmlr.JMLRScraper;
import org.bibsonomy.scraper.url.kde.journalogy.JournalogyScraper;
import org.bibsonomy.scraper.url.kde.jstor.JStorScraper;
import org.bibsonomy.scraper.url.kde.karlsruhe.AIFBScraper;
import org.bibsonomy.scraper.url.kde.karlsruhe.BibliographyScraper;
import org.bibsonomy.scraper.url.kde.karlsruhe.UBKAScraper;
import org.bibsonomy.scraper.url.kde.l3s.L3SScraper;
import org.bibsonomy.scraper.url.kde.langev.LangevScraper;
import org.bibsonomy.scraper.url.kde.librarything.LibrarythingScraper;
import org.bibsonomy.scraper.url.kde.liebert.LiebertScraper;
import org.bibsonomy.scraper.url.kde.mathscinet.MathSciNetScraper;
import org.bibsonomy.scraper.url.kde.metapress.MetapressScraper;
import org.bibsonomy.scraper.url.kde.muse.ProjectmuseScraper;
import org.bibsonomy.scraper.url.kde.nasaads.NasaAdsScraper;
import org.bibsonomy.scraper.url.kde.nature.NatureScraper;
import org.bibsonomy.scraper.url.kde.nber.NberScraper;
import org.bibsonomy.scraper.url.kde.opac.OpacScraper;
import org.bibsonomy.scraper.url.kde.openrepository.OpenrepositoryScraper;
import org.bibsonomy.scraper.url.kde.osa.OSAScraper;
import org.bibsonomy.scraper.url.kde.pion.PionScraper;
import org.bibsonomy.scraper.url.kde.plos.PlosScraper;
import org.bibsonomy.scraper.url.kde.pnas.PNASScraper;
import org.bibsonomy.scraper.url.kde.prola.ProlaScraper;
import org.bibsonomy.scraper.url.kde.psycontent.PsyContentScraper;
import org.bibsonomy.scraper.url.kde.pubmed.PubMedScraper;
import org.bibsonomy.scraper.url.kde.pubmedcentral.PubMedCentralScraper;
import org.bibsonomy.scraper.url.kde.rsoc.RSOCScraper;
import org.bibsonomy.scraper.url.kde.science.ScienceDirectScraper;
import org.bibsonomy.scraper.url.kde.sciencemag.ScienceMagScraper;
import org.bibsonomy.scraper.url.kde.scientificcommons.ScientificcommonsScraper;
import org.bibsonomy.scraper.url.kde.scopus.ScopusScraper;
import org.bibsonomy.scraper.url.kde.spires.SpiresScraper;
import org.bibsonomy.scraper.url.kde.springer.SpringerLinkScraper;
import org.bibsonomy.scraper.url.kde.springer.SpringerScraper;
import org.bibsonomy.scraper.url.kde.ssrn.SSRNScraper;
import org.bibsonomy.scraper.url.kde.usenix.UsenixScraper;
import org.bibsonomy.scraper.url.kde.wileyintersience.WileyIntersienceScraper;
import org.bibsonomy.scraper.url.kde.worldcat.WorldCatScraper;
import org.bibsonomy.scraper.url.kde.wormbase.WormbaseScraper;


/**
 * Contains all active UrlScrapers. 
 * 
 * @author rja
 *
 */
public class KDEUrlCompositeScraper extends UrlCompositeScraper {

	/**
	 * Public constructor adding the active scrapers.
	 */
	public KDEUrlCompositeScraper() {
		addScraper(new CiteBaseScraper());
		addScraper(new OpacScraper());
		addScraper(new IEEEXploreScraper());
		addScraper(new SpringerLinkScraper());
		addScraper(new ScienceDirectScraper());
		addScraper(new PubMedScraper());
		addScraper(new PubMedCentralScraper());
		addScraper(new SpiresScraper());
		addScraper(new L3SScraper());
		addScraper(new ACMBasicScraper());
		// addScraper(new CiteseerBasicScraper()); old citeseer scraper removed from chain 
		addScraper(new AIFBScraper());
		addScraper(new UBKAScraper());
		addScraper(new ArxivScraper());
		addScraper(new IngentaconnectScraper());
		addScraper(new LibrarythingScraper());
		addScraper(new NasaAdsScraper());
		addScraper(new AipScitationScraper());
		addScraper(new MathSciNetScraper());
		addScraper(new WileyIntersienceScraper());
		addScraper(new IOPScraper());
		addScraper(new ProlaScraper());
		addScraper(new BibSonomyScraper());
		addScraper(new IEEEComputerSocietyScraper());
		addScraper(new AmazonScraper());
		addScraper(new PlosScraper());
		addScraper(new NatureScraper());
		addScraper(new BlackwellSynergyScraper());
		addScraper(new DBLPScraper());
		addScraper(new BioMedCentralScraper());
		addScraper(new WorldCatScraper());
		addScraper(new SpringerScraper());
		addScraper(new ACSScraper());
		addScraper(new AnthroSourceScraper());
		addScraper(new BMJScraper());
		addScraper(new EditLibScraper());
		addScraper(new InformaWorldScraper());
		addScraper(new CambridgeScraper());
		addScraper(new LangevScraper());
		addScraper(new LiebertScraper());
		addScraper(new NberScraper());
		addScraper(new UsenixScraper());
		addScraper(new IucrScraper());
		addScraper(new OSAScraper());
		addScraper(new PsyContentScraper());
		addScraper(new RSOCScraper());
		addScraper(new PNASScraper());
		addScraper(new ScienceMagScraper());
		addScraper(new JStorScraper());
		addScraper(new EricScraper());
		addScraper(new IWAPonlineScraper());
		addScraper(new JMLRScraper());
		addScraper(new AclScraper());
		addScraper(new AnnualreviewsScraper());
		addScraper(new ProjectmuseScraper());
		addScraper(new SSRNScraper());
		addScraper(new ScopusScraper());
		addScraper(new MetapressScraper());
		addScraper(new CiteseerxScraper());
		addScraper(new OpenrepositoryScraper());
		addScraper(new PionScraper());
		addScraper(new CiteulikeScraper());
		addScraper(new AmsScraper());
		addScraper(new BibliographyScraper());
		addScraper(new WormbaseScraper());
		addScraper(new GoogleScholarScraper());
		addScraper(new DLibScraper());
		addScraper(new ScientificcommonsScraper());
		addScraper(new AGUScraper());
		addScraper(new CellScraper());
		addScraper(new IsiScraper());
		addScraper(new CasesJournalScraper());
		addScraper(new ElsevierhealthScraper());
		addScraper(new AandAScraper());
		addScraper(new JournalogyScraper());
	}

}
