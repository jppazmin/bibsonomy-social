/**
 *
 *  BibSonomy-Webapp - The webapplication for Bibsonomy.
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

package org.bibsonomy.webapp.command.admin;

import java.util.HashMap;
import java.util.Map;

import org.bibsonomy.common.enums.ClassifierAlgorithm;
import org.bibsonomy.common.enums.ClassifierMode;
import org.bibsonomy.common.enums.ClassifierSettings;

/**
 * Bean for classifier settings
 * 
 * @author Stefan Stützer
 * @author Beate Krause
 * @version $Id: AdminSettingsCommand.java,v 1.2 2008-04-07 13:25:48
 *          ss05sstuetzer Exp $
 */
public class AdminSettingsCommand {

	/** Day or night mode of classifier */
	private String mode;

	/** Algorithm of classifier */
	private String algorithm;

	/** Length of training period */
	private String trainingPeriod;

	/** Length of classification period */
	private String classifyPeriod;

	/** Probability limit (threshold for sure / not sure classification) */
	private String probabilityLimit;
	
	/** Costs of false positives */
	private String classificationCosts;
	
	/** White list expression to add into database */
	private String whitelistExp;

	/** Options to present different algorithms */
	private Map<String, String> algorithmOptions;
	
	/** Options to present different modes */
	private Map<String, String> modeOptions;

	/**
	 * inits the mode and algorithm options
	 */
	public AdminSettingsCommand() {
		/**
		 * initialize options
		 */
		//algorithm options
		// TODO enum in options übergeben
		algorithmOptions = new HashMap<String, String>();
		algorithmOptions.put(ClassifierAlgorithm.IBK.name(), ClassifierAlgorithm.IBK.getDescription());
		algorithmOptions.put(ClassifierAlgorithm.NAIVE_BAYES.name(), ClassifierAlgorithm.NAIVE_BAYES.getDescription());
		algorithmOptions.put(ClassifierAlgorithm.J48.name(), ClassifierAlgorithm.J48.getDescription());
		algorithmOptions.put(ClassifierAlgorithm.ONER.name(), ClassifierAlgorithm.ONER.getDescription());
		algorithmOptions.put(ClassifierAlgorithm.LOGISTIC.name(), ClassifierAlgorithm.LOGISTIC.getDescription());
		algorithmOptions.put(ClassifierAlgorithm.LIBSVM.name(), ClassifierAlgorithm.LIBSVM.getDescription());
		algorithmOptions.put(ClassifierAlgorithm.SMO.name(), ClassifierAlgorithm.SMO.getDescription());
		
		//mode options
		modeOptions = new HashMap<String, String>();
		modeOptions.put(ClassifierMode.DAY.name(),ClassifierMode.DAY.getAbbreviation());
		modeOptions.put(ClassifierMode.NIGHT.name(), ClassifierMode.NIGHT.getAbbreviation());
		
	}

	/**
	 * set options for classification task
	 * @param setting
	 * @param value
	 */
	public void setAdminSetting(final ClassifierSettings setting, final String value) {
		switch (setting) {
		case ALGORITHM:
			this.algorithm = value;
			break;
		case MODE:
			this.mode = value;
			break;
		case TRAINING_PERIOD:
			this.trainingPeriod = value;
			break;
		case CLASSIFY_PERIOD:
			this.classifyPeriod = value;
			break;
		case PROBABILITY_LIMIT:
			this.probabilityLimit = value;
			break;
		case CLASSIFY_COST:
			this.classificationCosts = value;
			break;
		}
		
	}
	
	/**
	 * @return the mode
	 */
	public String getMode() {
		return this.mode;
	}

	/**
	 * @return the algorithm
	 */
	public String getAlgorithm() {
		return this.algorithm;
	}

	/**
	 * @return the trainingPeriod
	 */
	public String getTrainingPeriod() {
		return this.trainingPeriod;
	}

	/**
	 * @param trainingPeriod the trainingPeriod to set
	 */
	public void setTrainingPeriod(String trainingPeriod) {
		this.trainingPeriod = trainingPeriod;
	}

	/**
	 * @return the algorithmOptions
	 */
	public Map<String, String> getAlgorithmOptions() {
		return this.algorithmOptions;
	}

	/**
	 * @param algorithmOptions the algorithmOptions to set
	 */
	public void setAlgorithmOptions(Map<String, String> algorithmOptions) {
		this.algorithmOptions = algorithmOptions;
	}

	/**
	 * @return the modeOptions
	 */
	public Map<String, String> getModeOptions() {
		return this.modeOptions;
	}

	/**
	 * @param modeOptions the modeOptions to set
	 */
	public void setModeOptions(Map<String, String> modeOptions) {
		this.modeOptions = modeOptions;
	}

	/**
	 * @return the classificationCosts
	 */
	public String getClassificationCosts() {
		return this.classificationCosts;
	}

	/**
	 * @param classificationCosts the classificationCosts to set
	 */
	public void setClassificationCosts(String classificationCosts) {
		this.classificationCosts = classificationCosts;
	}

	/**
	 * @return the whitelistExp
	 */
	public String getWhitelistExp() {
		return this.whitelistExp;
	}

	/**
	 * @param whitelistExp the whitelistExp to set
	 */
	public void setWhitelistExp(String whitelistExp) {
		this.whitelistExp = whitelistExp;
	}

	/**
	 * @return the classifyPeriod
	 */
	public String getClassifyPeriod() {
		return this.classifyPeriod;
	}

	/**
	 * @param classifyPeriod the classifyPeriod to set
	 */
	public void setClassifyPeriod(String classifyPeriod) {
		this.classifyPeriod = classifyPeriod;
	}

	/**
	 * @return the probabilityLimit
	 */
	public String getProbabilityLimit() {
		return this.probabilityLimit;
	}

	/**
	 * @param probabilityLimit the probabilityLimit to set
	 */
	public void setProbabilityLimit(String probabilityLimit) {
		this.probabilityLimit = probabilityLimit;
	}
}