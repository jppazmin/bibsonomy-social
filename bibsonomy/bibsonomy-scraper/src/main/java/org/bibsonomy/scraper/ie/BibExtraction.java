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

package org.bibsonomy.scraper.ie;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.naming.NamingException;

import edu.umass.cs.mallet.base.pipe.CharSequence2TokenSequence;
import edu.umass.cs.mallet.base.types.Instance;
import edu.umass.cs.mallet.base.types.TokenSequence;
import edu.umass.cs.mallet.util.bibsonomy.IEInterface;


/**
 * created: Sep 29, 2006
 *
 * this class labels references for bibsonomy 
 * 
 * @author Thomas Steuber
 **/
public class BibExtraction {

	private static final String reference1 = "E. Tyugu, H-M. Haav, J. Penjam et al. , Multiprocessing Computing System \"Elbrus-1\". System for Generating Problem-oriented Packages of Programs, Algorithms and Programs, Moscow 1982, No. 6(50) (in russian)";

	private static final String reference2 = "H-M. Haav, Tyugu E., Representation of data models using computational frames. In. Proceedings of the Third Soviet Database Conference, Tallinn, 1985 (in russian) pp 69-86";

	private static final String reference3 = "H-M. Haav, A universal language for representing data models, Applied Informatics, Moscow 1986, No 2(11) (in russian) pp 130-143";

	private static final String reference4 = "H-M. Haav, M. Koov, Expert system and databases for MicroPRIZ system, In. Personal Computers and their Applications. Abstracts of the papers presented at Soviet-Finnish International Symposium on Mini-, Micro- & Personal Computers and their Applications, Tallinn, 1986, pp 55-56";

	private static final String reference5 = "H-M. Haav, A. Kalja, M. Koov, M. Kopp, The System MicroPRIZ - a tool for development of intelligent application packages, Computing Systems and Software, Novosibirsk, 1986.(in russian) pp 89-100";

	private static final String reference6 = "H-M. Haav, Data processing facilities of ExperPRIZ, Computers and Data Processing, No 6 Tallinn, 1988 (in estonian) pp 5-8";

	private static final String reference7 = "H-M. Haav, Expert-Database Systems: the nearist future of database systems, In. Proceedings of Republican Scientific Seminar on Applied Artificial Intelligence and Information Systems, Tallinn 1989 (in estonian), pp 15-19";

	private static final String reference8 = "H-M. Haav, M. Koov, EXPERTPRIZ as an Environment for Developing Expert Database Systems, In. Proceedings of the Twelfth International Seminar on Database Management Systems, Suzdal (USSR), 1989, pp 89-94";

	private static final String reference9 = "H-M. Haav, Object-oriented Data Models, In. Proceedings of Soviet Conference on Actual Problems of System Programming: Object-oriented Programming, Tallinn 1990, (in russian), pp 60-63";

	private static final String reference10 = "H-M. Haav, Object-oriented data models: basic notions and implementation in the system NUT, Programmirovanie, No 6, 1990, pp 57-66 (in russian)";

	private static final String reference11 = "H-M. Haav, A. Kalja. Knowledge-Based Data Modelling Technique, In: Advances in Information Modelling and Knowledge Bases, IOS Press, Amsterdam, 1991, pp 152-166";

	private static final String reference12 = "H-M. Haav, Object-oriented paradigm and databases, Computers and Data Processing, No 2 1992, Tallinn pp 35-48";

	private static final String reference13 = "H-M. Haav, An Object-Oriented Approach to Conceptual Data Modelling, In: Information Modelling and Knowledge Bases: Foundations, Theory and Applications, IOS Press, Amsterdam, 1992, pp 333-347";

	private static final String reference14 = "H-M. Haav, M. Matskin, Using partial deduction for automatic propagation of changes in OODB In: Information Modelling and Knowledge Bases IV: Concepts, Methods and Systems, IOS Press, Amsterdam, 1993 pp 339-353";

	private static final String reference15 = "H-M. Haav, Modelling Evolution in Object-Oriented Databases, In. C. Beeri, A. Heuer, G. Saake, S. Urban (Eds.) Formal Aspects of Object Base Dynamics. Dagstuhl- Seminar-Report 62;(9317), 1993, pp 7-8";

	private static final String reference16 = "H-M. Haav, Specifying Semantics of Evolution in Object-Oriented Databases Using Partial Deduction, In: U. W. Lipeck, B. Thalheim (eds.), Modelling Database Dynamics. Selected Papers from the Fourth International Workshop on Foundations of Models and Languages for Data and Objects, Volkse, Germany, Springer-Verlag, 1993 pp 48-63";

	private static final String reference17 = "H-M. Haav, A. Kalja, Databases for Systems with Structural Synthesis of Programs, In: Proc. of the Baltic Workshop on National Infrastructure Databases: Problems, Methods, Experiences, Vilnius, Mokslo Aidai, 1994, pp 185-196";

	private static final String reference18 = "H-M. Haav, A Meta-language for Specification of Evolving Class and Object Lattices in Object-oriented Databases, In. Preprint of Proc. of the Second International East-West Database Workshop, Sept. 25-28 , 1994, Klagenfurt, Austria , pp 213-224";

	private static final String reference19 = "H-M. Haav, Towards Evolving Class and Object Lattices (in OODB), In: Preprint of the Fourth European-Japanese Seminar on Information Modelling and Knowledge Bases, Kista, Sweden, 1994, Section 26 (pp 1-13)";

	private static final String reference20 = "J. A. Bubenko Jr., A. Caplinskas, J. Grundspenkis, H.-M. Haav, A. Solvberg (Eds) Proc. of the Baltic Workshop on National Infrastructure Databases: Problems, Methods, Experiences, Vol. 1-2, Vilnius, Mokslo Aidai, 1994.";

	private static final String reference21 = "H-M. Haav, A Meta-language for Representation of Evolution Invariants in OODB, In. H-D. Ehrich, G. Engels, J. Paredaens, P. Wegner (Eds.) Fundamentals of Object-Oriented Languages, Systems, and Methods. Dagstuhl-Seminar-Report 95;(9434), 1994, p 17";

	private static final String reference22 = "Matskin M, and Haav H.-M., A Deductive Object-Oriented Approach to Information System Modeling, In: Proc. of the Second Int. Workshop on Advances in Databases and Information Systems, Moscow, 1995, pp 267-276";

	private static final String reference23 = "Haav H.-M., Towards Evolving Class and Object Lattices (in OODB). In: Eds. H. Kangassalo et al., Information Modelling and Knowledge Bases VI, IOS Press, Amsterdam, 1995, pp. 413-427";

	private static final String reference24 = "H-M. Haav, A Meta-language for Specification of Evolving Class and Object Lattices in Object-oriented Databases, In. J. Eder and L.A. Kalinitchenko (Eds) East/West Database Workshop, Proc. of the Second International East-West Database Workshop, Klagenfurt,1994 , Workshops In Computing Series, Springer-Verlag 1995, pp 147-159";

	private static final String reference25 = "H-M. Haav, A Flexible Model for Dynamic (Re)classification of Objects, Institute of Cybernetics, Research Report CS 82/95";

	private static final String reference26 = "Matskin M, and Haav H.-M., A Deductive Object-Oriented Approach to Information System Modeling, In: J. Eder, L.A. Kalinichenko (Eds) Advances in Databases and Information Systems, Workshops In Computing Series, Springer, 1996, pp 459-479";

	private static final String reference27 = "Haav H-M, Thalheim B. (Eds), Databases and Information Systems, Proc. of the Second International Baltic Workshop, Tallinn, June12-14, 1996, Vol. I-Research Track, 232 p, Vol. II- Technology Track, 178 p, Tampere University of Technology Press, 1996";

	private static final String reference28 = "Haav H-M, An Object Classifier Based on Galois Approach, In: H. Kangassalo, J. F. Nilsson, H. Jaakkola, S. Ohsuga, Information Modelling and Knowledge Bases VIII, IOS Press, 1997, pp 309-321";

	private static final String reference29 = "J. F. Nilsson, H-M. Haav, Inducing queries from examples as concept formation, In: Proc. of the 8th European-Japanese Conference on Information Modelling and Knowledge Bases, May 26-29, 1998, Finland, pp 341-353";

	private static final String reference30 = "H-M. Haav, M. Matskin, Discovery of Object-oriented Schema and Schema Conflicts, W. Litwin, T. Morzy, G. Vossen (Eds), Advances in Databases and Information Systems, ADBIS�98, LNCS 1475, Springer, 1998, pp 154-157";

	private static final String reference31 = "J. F. Nilsson, H-M. Haav, Inducing queries from examples as concept formation, H. Jakkola, H. Kangassalo, E. Kawaguchi (Eds), Information Modelling and Knowledge Bases X, IOS Press, 1999, pp 103-125";

	private static final String reference32 = "H-M. Haav, Virtual University as a Web-based Information System, J.  Eder, I. Rozman, T. Welzer (Eds), Proc. of the Third East-EuropeanConference on Advances in Databases and Information Systems, Maribor, Slovenia, Sept. 13-16, 1999, pp 61-68";

	private static final String reference33 = "H-M. Haav, J. F. Nilsson , Approaches to Concept Based Exploration of Information Resources, W. Abramowicz and J. Zurada (Eds), Knowledge Discovery for Business Information Systems, Kluwer Academic Publishers, 2001, ch 4, pp 89-109";

	private static final String reference34 = "H-M. Haav, T.-L. Lubi, A Survey of Concept-based Information Retrieval Tools on the Web, A. Caplinkas and J. Eder (Eds), Advances in Databases and Information Systems, Proc. of 5th East-European Conference ADBIS*2001, Vol 2., Vilnius \"Technika\" 2001, pp 29-41";

	private static final String reference35 = "H-M. Haav, A. Kalja (Eds), Databases and Information Systems, Proceedings of the Fifth International Baltic Conference, BalticDB&IS�2002, Tallinn, June 3-6, 2002, Institute of Cybernetics at TTU, Tallinn, 2002, Vol. 1, pp. 330, Vol. 2 pp. 308";

	private static final String reference36 = "H-M. Haav, A. Kalja (Eds), Databases and Information Systems II, Selected Papers from the Fifth International Baltic Conference, BalticDB&IS�2002, Kluwer Academic Publishers, 2002, pp 320 ";

	private static final String reference37 = "H-M. Haav, Learning Ontologies for Domain-specific Information Retrieval, W. Abramowicz (Ed), Knowledge-Based Information Retrieval and Filtering from the Web, Kluwer Academic Publishers, 2003, ch 15, pp 285-300";

	private static final String reference38 = "H-M. Haav, An Application of Inductive Concept Analysis to Construction of Domain-specific Ontologies, In: B. Thalheim, Gunar Fiedler (Eds), Emerging Database Research in East Europe, Proceedings of the Pre-conference Workshop of VLDB 2003, Computer Science Reports, Brandenburg University of Technology at Cottbus, 2003, 14/3, pp 63-67  ";

	private static final String reference39 = "H-M. Haav, Combining FCA and a Logic Language for Ontology Representations, J. Barzdins (Ed), Databases and Information Systems, Sixth International Baltic Conference on Databases and Information Systems, June 6-9, Riga, 2004, Scientific papers of University of Latvia, Vol 672, 2004, pp 436-451 ";

	private static final String reference40 = "H-M. Haav, A Semi-automatic Method to Ontology Design by Using FCA, V. Snasel, R. Belohlavek (Eds), Concept Lattices and their Applications, Proceedings of the 2nd International CLA Workshop, TU of Ostrava, 2004, pp 13-25 ( also published in corresponding CEUR Workshop Proceedings at http://ceur-ws.org/Vol-110)";

	private static final String reference41 = "H-M. Haav, Combining FCA and a Logic Language for Ontology Representation, J. Barzdins and A. Caplinskas (Eds), Databases and Information Systems, Selected Papers from the Sixth International Baltic Conference DB&IS�2004, IOS Press, 2005, pp 259-270";

	private static final String reference42 = "H.-M. Haav, An Ontology Learning and Reasoning Framework, Proceedings of 15th European-Japanese Conference on Information Modelling and Knowledge Bases, May 16-19, 2005, Tallinn, pp 198-206";

	private static final String reference43 = "J. Eder, H.-M. Haav, A. Kalja, J. Penjam (Eds.),  Advances in Databases and Information Systems, Proceedings of Ninth East European Conference ADBIS 2005, LNCS 3631, Springer, 2005, pp 392";

	private static final String reference44 = "J. Eder, H.-M. Haav, A. Kalja, J. Penjam (Eds.),  Advances in Databases and Information Systems, Proceedings of Ninth East European Conference ADBIS 2005, Tallinn University of Technology Press, 2005, pp 263";

	private static final String reference45 = "H.-M. Haav, An Ontology Learning and Reasoning Framework,  Information Modelling and Knowledge Bases XVII, Y. Kiyoki et al. (Eds), IOS Press, 2006, pp 302-310";

	private static final String reference46 = "T. Tammet, H-M. Haav, V. Kadarpik, m. K��ramees, a Rule-based Approach to Web-based Application Development, O. Vacilecas et al (Eds), Proc. of  the 2006 Seventh International Baltic Conference on Databases and Information Systems, IEEE, 2006, pp 202-211. IEEE Xplorer database: http://ieeexplore.ieee.org/xpl/tocresult.jsp?isnumber=35308&isYear=2006";

	private static final String reference47 = "R. Bekkerman, M.Sahami, and E. Learned-Miller. Combinatorial Markov Random Fields. In Proceedings of ECML 2006";

	private static final String reference48 = "R. Bekkerman and M.Sahami. Semi-supervised Clustering using Combinatorial MRFs. In Proceedings of ICML 2006 Workshop on Learning in Structured Output Spaces";

	private static final String reference49 = "R. Bekkerman, R. El-Yaniv, and A. McCallum. Multi-way Distributional Clustering via Pairwise Interactions. In Proceedings of ICML 2005";

	private static final String reference50 = "R. Bekkerman and A. McCallum. Disambiguating Web Appearances of People in a Social Network. In Proceedings of WWW 2005";

	private static final String reference51 = "R. Bekkerman, A. McCallum, and G. Huang. Automatic Categorization of Email into Folders: Benchmark Experiments on Enron and SRI Corpora. CIIR Technical Report IR-418 2004";

	private static final String reference52 = "A. Culotta, R. Bekkerman, and A. McCallum. Extracting Social Networks and Contact Information from Email and the Web. In Proceedings of CEAS 2004";

	private static final String reference53 = "R. Bekkerman and J. Allan. Using Bigrams in Text Categorization. CIIR Technical Report IR-408 2004";

	private static final String reference54 = "R. Bekkerman. Word Distributional Clustering for Text Categorization. MSc Thesis 2003, Technion � Israel Institute of Technology";

	private static final String reference55 = "R. Bekkerman, R. El-Yaniv, N. Tishby, and Y. Winter. Distributional Word Clusters vs. Words for Text Categorization. In Special Issue on Variable and Feature Selection of JMLR 2003";

	private static final String reference56 = "R. Bekkerman, R. El-Yaniv, N. Tishby, and Y. Winter. On Feature Distributional Clustering for Text Categorization. In Proceedings of SIGIR 2001";
	
	private static final String reference57 = "B. Berendt, A. Hotho, G. Stumme (Eds.) : Semantic Web Mining. Proc. of the Semantic Web Mining Workshop of the 13th Europ. Conf. on Machine Learning (ECML'02) / 6th Europ. Conf. on Principles and Practice of Knowledge Discovery in Databases (PKDD'02), Helsinki, August 19, 2002";
	
	private static final String reference58 = "S. Basu, A. Banerjee, R.Mooney, Semi-supervised Clustering by Seeding, 19th ICML, 2002.";

	private static final String reference59 = "S. Basu, M. Bilenko and R. J. Mooney, Active Semi-Supervision for Pairwise Constrained Clustering, 4th SIAM Data Mining Conf.. 2004.";
	
	private static final String reference60 = "P. Bradley, U. Fayyad, and C. Reina, �Scaling Clustering Algorithms to Large Databases�, 4th ACM KDD Conference. 1998.";
	
	private static final String reference61 = "I. Davidson and S. S. Ravi, �Clustering with Constraints: Feasibility Issues and the k-Means Algorithm�, SIAM International Conference on Data Mining, 2005.";
	
	private static final String reference62 = "I. Davidson and S. S. Ravi, �Towards Efficient and Improved Hierarchical Clustering with Instance and Cluster-Level Constraints�, Tech. Report, CS Department, SUNY - Albany, 2005. Available from: www.cs.albany.edu/davidson";
	
	private static final String reference63 = "C. Elkan, Using the triangle inequality to accelerate k-means, ICML, 2003.";
	
	private static final String reference64 = "M. Garey and D. Johnson, Computers and Intractability: A Guide to the Theory of NPcompleteness, Freeman and Co., 1979.";
	
	private static final String reference65 = "M. Garey, D. Johnson and H.Witsenhausen, �The complexity of the generalized Lloyd-Max problem�, IEEE Trans. Information Theory, Vol. 28,2, 1982.";
	
	private static final String reference66 = "D. Klein, S. D. Kamvar and C. D. Manning, �From Instance-Level Constraints to Space-Level Constraints: Making the Most of Prior Knowledge in Data Clustering�, ICML 2002.";
	
	private static final String reference67 = "M. Nanni, Speeding-up hierarchical agglomerative clustering in presence of expensive metrics, PAKDD 2005, LNAI 3518.";
	
	private static final String reference68 = "T. J. Schafer, �The Complexity of Satisfiability Problems�, STOC, 1978.";
	
	private static final String reference69 = "K. Wagstaff and C. Cardie, �Clustering with Instance-Level Constraints�, ICML, 2000.";
	
	private static final String reference70 = "D. B. West, Introduction to Graph Theory, Second Edition, Prentice-Hall, 2001.";
	
	private static final String reference71 = "K. Yang, R. Yang,M. Kafatos, �A FeasibleMethod to Find Areas with Constraints Using Hierarchical Depth-First Clustering�, Scientific and Stat. Database Management Conf., 2001.";
	
	private static final String reference72 = "O. R. Zaiane, A. Foss, C. Lee, W. Wang, On Data Clustering Analysis: Scalability, Constraints and Validation, PAKDD, 2000.";
	
	private static final String reference73 = "Y. Zho & G. Karypis, �Hierarchical Clustering Algorithms for Document Datasets�, Data Mining and Knowledge Discovery, Vol. 10 No. 2, March 2005, pp. 141�168.";
	
	private static final String reference74 = "A. Cau, R. Kuiper, and W.-P. de Roever. Formalising Dijkstra's development strategy within Stark's formalism. In C. B. Jones, R. C. Shaw, and T. Denvir, editors, Proc. 5th. BCS-FACS Refinement Workshop, 1992."; 
	
	private static final String reference75 = "M. Kitsuregawa, H. Tanaka, and T. Moto-oka. Application of hash to data base machine and its architecture. New Generation Computing, 1(1), 1983.";
	
	private static final String reference76 = "Alex�nder Vrchoticky. Modula/R language definition. Technical Report TU Wien rr-02-92, version 2.0, Dept. for Real-Time Systems, Technical University of Vienna, May 1993.";


	private static Pattern regex = Pattern.compile ("\\p{Alpha}+|\\p{Digit}+|\\p{Punct}");

	public HashMap<String, String> extraction(String fileName, String reference){
		IEInterface ieInterface = new IEInterface();//crf #2

		ieInterface.loadCRF(new File(fileName));

		try {
			Instance instance = new Instance(reference, null, "reference", null);
			CharSequence2TokenSequence cs2ts = new CharSequence2TokenSequence(regex); 
						
			ieInterface.viterbiCRFTokenSequence((TokenSequence)cs2ts.pipe(instance).getData());
			return ieInterface.printResultInHashMap();
		} catch(Exception e) {
			System.err.println();
			System.err.println("Exception ");
			e.printStackTrace();
			System.err.println("reference " + reference);
			System.err.println();
		}
		return null;

	}

	
	public HashMap<String, String> extraction(String reference) throws IOException, ClassNotFoundException, NamingException{
		final IEInterface ieInterface = new IEInterface();
		ieInterface.loadCRF(new CRFSingleton().getCrf());

		try {
			final Instance instance = new Instance(reference, null, "reference", null);
			CharSequence2TokenSequence cs2ts = new CharSequence2TokenSequence(regex); 
			
			ieInterface.viterbiCRFTokenSequence((TokenSequence)cs2ts.pipe(instance).getData());
			return ieInterface.printResultInHashMap();
		} catch (Exception e) {
			// TODO: logging?
			System.err.println();
			System.err.println("Exception ");
			e.printStackTrace();
			System.err.println("reference " + reference);
			System.err.println();
		}
		return null;

	}
	
	
	// TODO: remove?
	public static void main (String[] args) throws FileNotFoundException
	{
		LinkedList<String> referenceList = new LinkedList<String>();
		/*referenceList.add(reference1);
		referenceList.add(reference2);
		referenceList.add(reference3);
		referenceList.add(reference4);
		referenceList.add(reference5);
		referenceList.add(reference6);
		referenceList.add(reference7);
		referenceList.add(reference8);
		referenceList.add(reference9);
		referenceList.add(reference10);
		referenceList.add(reference11);
		referenceList.add(reference12);
		referenceList.add(reference13);
		referenceList.add(reference14);
		referenceList.add(reference15);
		referenceList.add(reference16);
		referenceList.add(reference17);
		referenceList.add(reference18);
		referenceList.add(reference19);
		referenceList.add(reference20);
		referenceList.add(reference21);
		referenceList.add(reference22);
		referenceList.add(reference23);
		referenceList.add(reference24);
		referenceList.add(reference25);
		referenceList.add(reference26);
		referenceList.add(reference27);
		referenceList.add(reference28);
		referenceList.add(reference29);
		referenceList.add(reference30);
		referenceList.add(reference31);
		referenceList.add(reference32);
		referenceList.add(reference33);
		referenceList.add(reference34);
		referenceList.add(reference35);
		referenceList.add(reference36);
		referenceList.add(reference37);
		referenceList.add(reference38);
		referenceList.add(reference39);
		referenceList.add(reference40);
		referenceList.add(reference41);
		referenceList.add(reference42);
		referenceList.add(reference43);
		referenceList.add(reference44);
		referenceList.add(reference45);
		referenceList.add(reference46);
		referenceList.add(reference47);
		referenceList.add(reference48);
		referenceList.add(reference49);
		referenceList.add(reference50);
		referenceList.add(reference51);
		referenceList.add(reference52);
		referenceList.add(reference53);
		referenceList.add(reference54);
		referenceList.add(reference55);
		referenceList.add(reference56);
		referenceList.add(reference57);
		referenceList.add(reference58);
		referenceList.add(reference59);
		referenceList.add(reference60);
		referenceList.add(reference61);
		referenceList.add(reference62);
		referenceList.add(reference63);
		referenceList.add(reference64);
		referenceList.add(reference65);
		referenceList.add(reference66);
		referenceList.add(reference67);
		referenceList.add(reference68);
		referenceList.add(reference69);
		referenceList.add(reference70);
		referenceList.add(reference71);
		referenceList.add(reference72);
		referenceList.add(reference73);
		referenceList.add(reference74);
		referenceList.add(reference75);*/
		referenceList.add(reference75);
		
		//referenceList.add(reference1);
		
		long timeStart = System.currentTimeMillis();

		IEInterface ieInterface = new IEInterface(new File("D:\\sourcecode\\eclipse-workspace\\mainbranch\\crfBibsonomy\\crf.dat"));

		ieInterface.loadCRF();

		for(String reference: referenceList){

			try{
				Instance instance = new Instance(reference, null, "reference", null);
				CharSequence2TokenSequence cs2ts = new CharSequence2TokenSequence(regex); 
				TokenSequence result = (TokenSequence)cs2ts.pipe(instance).getData();
				
				ieInterface.viterbiCRFTokenSequence(result);

//				ieInterface.viterbiCRFTokenSequence((TokenSequence)cs2ts.pipe(instance).getData());
				HashMap<String, String> map = ieInterface.printResultInHashMap();
				System.out.println(reference);
				System.out.println();
				String author = map.get("author");
				System.out.println("information extraktion:");
				System.out.println("author=" + author);
				System.out.println("title=" + map.get("title"));
				System.out.println("date=" + map.get("date"));
				System.out.println("publisher=" + map.get("publisher"));
				System.out.println("location=" + map.get("location"));
				System.out.println("pages=" + map.get("pages"));
				System.out.println("institution=" + map.get("institution"));
				System.out.println("editor=" + map.get("editor"));
				System.out.println("volume=" + map.get("volume"));
				System.out.println("note=" + map.get("note"));
				System.out.println("booktitle=" + map.get("booktitle"));
				System.out.println("tech=" + map.get("tech"));
				System.out.println("journal=" + map.get("journal"));

				System.out.println();
				//System.out.println(ieInterface.printResultInFormat(true));
				System.out.println();
			}catch(Exception e){
				System.err.println();
				System.err.println("Exception " + e.toString());
				System.err.println("reference " + reference);
				System.err.println();
			}
		}
		
		/*BibExtraction test = new BibExtraction();
		try{
			HashMap<String, String> map = test.extraction("crf-saved-2.dat", reference1);
			
			System.out.println(map);
		}catch(Exception e){
			e.printStackTrace();
		}*/
	}
	
}
