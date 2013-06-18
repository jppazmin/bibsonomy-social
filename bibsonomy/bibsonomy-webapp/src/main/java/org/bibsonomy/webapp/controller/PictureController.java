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

package org.bibsonomy.webapp.controller;

import static org.bibsonomy.util.ValidationUtils.present;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.bibsonomy.common.enums.ProfilePrivlevel;
import org.bibsonomy.common.enums.UserRelation;
import org.bibsonomy.model.User;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.util.StringUtils;
import org.bibsonomy.util.file.FileUtil;
import org.bibsonomy.util.upload.FileUploadInterface;
import org.bibsonomy.util.upload.impl.FileUploadFactory;
import org.bibsonomy.webapp.command.actions.PictureCommand;
import org.bibsonomy.webapp.util.ErrorAware;
import org.bibsonomy.webapp.util.MinimalisticController;
import org.bibsonomy.webapp.util.RequestLogic;
import org.bibsonomy.webapp.util.RequestWrapperContext;
import org.bibsonomy.webapp.util.View;
import org.bibsonomy.webapp.view.ExtendedRedirectView;
import org.bibsonomy.webapp.view.Views;
import org.springframework.validation.Errors;

/**
 * this controller returns handles picture upload and download
 * @author wla
 * @version $Id: PictureController.java,v 1.12 2011-03-21 10:31:45 dbe Exp $
 */
public class PictureController implements MinimalisticController<PictureCommand>, ErrorAware {

	static {
		/*
		 * set the headless mode for awt library
		 * FIXME does it work? Should we really do this here? Better in a 
		 * Tomcat config file, right?!
		 */
		System.setProperty("java.awt.headless", "true");
	}

	private static final String FILE_EXTENSION = ".jpg";
	/**
	 * We need an admin logic to check the friends and the privlevel of the
	 * requested user.
	 */
	private LogicInterface adminLogic;
	private RequestLogic requestLogic;
	/**
	 * the factory used to get an instance of a FileUploadHandler.
	 */
	private FileUploadFactory uploadFactory;

	private Errors errors = null;

	/**
	 * Path to the picture folder
	 */
	private String path;
	private int sizeOfLargestSide;
	private String defaultFileName;

	@Override
	public PictureCommand instantiateCommand() {
		return new PictureCommand();
	}

	@Override
	public View workOn(PictureCommand command) {
		final String method = requestLogic.getMethod();
		if ("GET".equals(method)) { 
			/*
			 * picture download
			 */
			return downloadPicture(command);
		} else if("POST".equals(method)) {
			/*
			 *  picture upload
			 */
			final Views view = uploadPicture(command);
			if (present(view)) {
				return view;
			}
			return new ExtendedRedirectView("/settings");
		} else { 
			return Views.ERROR;
		}

	}

	/**
	 * Returns a view with the requested picture.
	 * 
	 * @param command
	 * @return
	 */
	private View downloadPicture(final PictureCommand command) {
		final String requestedUserName = command.getRequestedUser();
		final String loginUserName = command.getContext().getLoginUser().getName();
		final User requestedUser = adminLogic.getUserDetails(requestedUserName);

		/* 
		 * if the owner of the picture wasn't found
		 * FIXME: we check presence of user name not user, because getUserDetails
		 * ALWAYS returns a user! This is probably not a good idea ... 
		 */
		if (!present(requestedUser.getName())) {
			return Views.ERROR;
		}

		if (pictureVisible(requestedUser, loginUserName)) {
			setProfilePicture(command, requestedUserName);
		} else {
			setDummyPicture(command);
		}
		return Views.DOWNLOAD_FILE;
	}

	/**
	 * Checks if the loginUser may see the profile picture of the requested user.
	 * Depends on the profile privlevel of the requested user.
	 * 
	 * @param requestedUser
	 * @param loginUserName
	 * @return
	 */
	private boolean pictureVisible(final User requestedUser, final String loginUserName) {
		final String requestedUserName = requestedUser.getName();
		/*
		 * login user may always see his/her photo
		 */
		if (requestedUserName.equals(loginUserName)) return true;
		/*
		 * Check the visibility depending on the profile privacy level.
		 */
		final ProfilePrivlevel visibility = requestedUser.getSettings().getProfilePrivlevel();
		switch(visibility) {
		case PRIVATE:
			return requestedUserName.equals(loginUserName);
		case PUBLIC:
			return true;
		case FRIENDS:
			if (present(loginUserName)) {
				final List<User> friends = adminLogic.getUserRelationship(requestedUserName, UserRelation.OF_FRIEND, null);
				for (final User friend : friends) {
					if (loginUserName.equals(friend.getName())) {
						return true;
					}
				}
			}
			//$FALL-THROUGH$
		default:
			return false;
		}
	}

	/**
	 * This method searches for user picture and selects it to show
	 * 
	 * @param command
	 * @param requestedUserName
	 */
	private void setProfilePicture(final PictureCommand command, final String requestedUserName) {
		/*
		 * verify existence of picture, and set it to view 
		 */
		final File picture = new File(getPicturePath(requestedUserName));
		if (picture.exists()) { 
			command.setPathToFile(picture.getAbsolutePath());
			command.setContentType(FileUtil.getContentType(picture.getName()));
			command.setFilename(requestedUserName + FILE_EXTENSION);
		} else {
			setDummyPicture(command);
		}
	}

	/**
	 * Constructs the path to the picture, given the user name.
	 * 
	 * @param userName
	 * @return
	 */
	private String getPicturePath(final String userName) {
		/*
		 * pattern of the name of picture file: "hash(username).jpg"
		 */
		final String fileName = StringUtils.getMD5Hash(userName) + FILE_EXTENSION;

		/*
		 * pictures are in the different folders, named by the fist
		 * two signs of hash
		 */
		return FileUtil.getFilePath(path, fileName);
	}


	/**
	 * this method selects a dummy picture to show
	 * @param command
	 */
	private void setDummyPicture(final PictureCommand command) {
		command.setPathToFile(path + defaultFileName);
		command.setContentType(FileUtil.getContentType(command.getPathToFile()));
		command.setFilename(defaultFileName);
	}

	/**
	 * This method manage the picture upload
	 * 
	 * @param command
	 * @return Error view or null if upload successful 
	 */
	private Views uploadPicture(final PictureCommand command) {
		final RequestWrapperContext context = command.getContext();

		// check if user is logged in, if not throw an error and go directly
		// back to uploadPage
		if (!context.isUserLoggedIn()) {
			errors.reject("error.general.login");
			return Views.ERROR;
		}

		/*
		 * check credentials to fight CSRF attacks 
		 */
		if (!context.isValidCkey()) {
			errors.reject("error.field.valid.ckey");
			return Views.ERROR;
		}

		final String loginUserName = context.getLoginUser().getName();

		if (present(command.getFile()) && command.getFile().getSize() > 0) {
			/*
			 * a file is given --> save it
			 */
			try {
				final FileUploadInterface uploadFileHandler = this.uploadFactory.getFileUploadHandler(Collections.singletonList(command.getFile().getFileItem()), FileUploadInterface.pictureExt);
				final File file = uploadFileHandler.writeUploadedFile().getFile();

				/*
				 * scale picture
				 */
				final BufferedImage scaledPicture = scalePicture(file);

				/*
				 * delete temporary file
				 */
				file.delete();

				/*
				 * check existence of target folder
				 */
				final File directory = new File(FileUtil.getFileDir(path, StringUtils.getMD5Hash(loginUserName)));
				if (!directory.exists()) {
					directory.mkdir();
				}
				
				/*
				 * write scaled image to disk
				 */
				ImageIO.write(scaledPicture, "jpeg", new File(getPicturePath(loginUserName)));
			} catch (Exception ex) {
				errors.reject("error.upload.failed", new Object[] { ex.getLocalizedMessage() }, "Sorry, we could not process your upload request, an unknown error occurred.");
				return Views.ERROR;
			}
		} else { 
			/*
			 * no file given, but POST request --> delete picture
			 */
			deleteUserPicture(loginUserName);
		}

		return null;
	}

	/**
	 * Deletes the picture from the file system
	 * 
	 * @param userName
	 */
	private void deleteUserPicture(final String userName) {
		final File picture = new File(getPicturePath(userName));
		if (picture.exists()) {
			picture.delete();
		}
	}

	@Override
	public Errors getErrors() {
		return errors;
	}

	@Override
	public void setErrors(final Errors errors) {
		/*
		 * here: check for binding errors
		 */
		this.errors = errors;
	}
	
	/**
	 * Scales the picture to a standard size.
	 * 
	 * @param imageFile
	 * @return ready to write BufferedImage
	 * @throws IOException
	 */
	private BufferedImage scalePicture(final File imageFile) throws IOException {
		final Image image = ImageIO.read(imageFile);
		final Image scaledImage;
		final int width = image.getWidth(null);
		final int height = image.getHeight(null);
		if (height > sizeOfLargestSide || width > sizeOfLargestSide) {
			/*
			 * convert picture to the standard size with fixed aspect ratio
			 */
			if (width > height) {
				/*
				 *  _________        ____
				 * |         | ---> |____|
				 * |_________|
				 * 
				 */
				scaledImage = image.getScaledInstance(sizeOfLargestSide, -1, Image.SCALE_SMOOTH);
			} else {
				/*
			 	*  ____        __
			 	* |    | ---> |  |
			 	* |    |      |__|
			 	* |    |
			 	* |____|
			 	*/
				scaledImage = image.getScaledInstance(-1, sizeOfLargestSide, Image.SCALE_SMOOTH);
			}
		} else {
			scaledImage = image;
		}

		/*
		 * create new BufferedImage with converted picture
		 */
		final BufferedImage outImage = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
		final Graphics g = outImage.getGraphics();
		g.drawImage(scaledImage, 0, 0, null);
		g.dispose();

		return outImage;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * This controller needs an admin logic to check the profile privacy level 
	 * and the friends of the requested user - something the login user is not
	 * allowed to do.
	 * 
	 * @param adminLogic the adminLogic to set
	 */
	public void setAdminLogic(LogicInterface adminLogic) {
		this.adminLogic = adminLogic;
	}

	/**
	 * @return the adminLogic
	 */
	public LogicInterface getAdminLogic() {
		return adminLogic;
	}

	/**
	 * @param requestLogic the requestLogic to set
	 */
	public void setRequestLogic(RequestLogic requestLogic) {
		this.requestLogic = requestLogic;
	}

	/**
	 * @return the requestLogic
	 */
	public RequestLogic getRequestLogic() {
		return requestLogic;
	}

	/**
	 * @param uploadFactory the uploadFactory to set
	 */
	public void setUploadFactory(FileUploadFactory uploadFactory) {
		this.uploadFactory = uploadFactory;
	}

	/**
	 * @return the uploadFactory
	 */
	public FileUploadFactory getUploadFactory() {
		return uploadFactory;
	}

	/**
	 * @param sizeOfLargestSide the sizeOfLargestSide to set
	 */
	public void setSizeOfLargestSide(int sizeOfLargestSide) {
		this.sizeOfLargestSide = sizeOfLargestSide;
	}

	/**
	 * @return the sizeOfLargestSide
	 */
	public int getSizeOfLargestSide() {
		return sizeOfLargestSide;
	}

	/**
	 * @param defaultFileName the defaultFileName to set
	 */
	public void setDefaultFileName(String defaultFileName) {
		this.defaultFileName = defaultFileName;
	}

	/**
	 * @return the defaultFileName
	 */
	public String getDefaultFileName() {
		return defaultFileName;
	}

}
