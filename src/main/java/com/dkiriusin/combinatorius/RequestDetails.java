/**
 * Copyright (c) 2015 deniskir@gmail.com. All rights reserved.
 *
 * @author Denis Kiriusin
 */

package com.dkiriusin.combinatorius;

/**
 * An immutable, thread-safe object that represents request details of Combinatorius servlet. 
 * The details include: {@link MimeType}, version, themeName resources and extension.
 * <p>
 * The object utilises Builder design pattern to simplify it's usage and to guarantee 
 * the consistency of the object after creation.
 */
class RequestDetails {

	private final MimeType mimeType;
	private final long version;
	private final String themeName;
	private final String resources;
	private final String extension;

	public static class Builder {
		// required parameters
		private final MimeType mimeType;
		// optional parameters initialised
		private String themeName = null;
		private String resources = null;
		private long version = 0;

		public Builder(MimeType mimeType) {
			this.mimeType = mimeType;
		}

		public Builder themeName(String themeName) {
			this.themeName = themeName;
			return this;
		}

		public Builder resources(String resources) {
			this.resources = resources;
			return this;
		}

		public Builder version(long version) {
			this.version = version;
			return this;
		}

		public RequestDetails build() {
			return new RequestDetails(this);
		}
	}

	private RequestDetails(Builder builder) {
		this.mimeType = builder.mimeType;
		this.version = builder.version;
		this.themeName = builder.themeName;
		this.resources = builder.resources;
		this.extension = this.mimeType == MimeType.css ? "css" : "js";
	}

	// Getters

	public MimeType getMimeType() {
		return mimeType;
	}

	public long getVersion() {
		return version;
	}

	public String getThemeName() {
		return themeName;
	}

	public String getResources() {
		return resources;
	}

	public String getExtension() {
		return extension;
	}

	// Overridden methods

	@Override
	public String toString() {
		return "RequestDetails [version=" + version + ", theme=" + themeName + ", resources=" + resources + ", extension="
				+ extension + ", mimeType=" + mimeType.getName() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((extension == null) ? 0 : extension.hashCode());
		result = prime * result
				+ ((mimeType == null) ? 0 : mimeType.hashCode());
		result = prime * result
				+ ((resources == null) ? 0 : resources.hashCode());
		result = prime * result + ((themeName == null) ? 0 : themeName.hashCode());
		result = prime * result + (int) (version ^ (version >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RequestDetails))
			return false;
		RequestDetails other = (RequestDetails) obj;
		if (extension == null) {
			if (other.extension != null)
				return false;
		} else if (!extension.equals(other.extension))
			return false;
		if (mimeType != other.mimeType)
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		if (themeName == null) {
			if (other.themeName != null)
				return false;
		} else if (!themeName.equals(other.themeName))
			return false;
		if (version != other.version)
			return false;
		return true;
	}
}
