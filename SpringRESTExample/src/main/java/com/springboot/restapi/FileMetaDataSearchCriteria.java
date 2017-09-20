package com.springboot.restapi;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class FileMetaDataSearchCriteria {

	private String name;

	private String descr;

	private String path;

	private LocalDateTime createdDateFrom;

	private LocalDateTime createdDateTo;

	private LocalDateTime lastUpdateDateFrom;

	private LocalDateTime lastUpdateDateTo;

	private String contentType;

	private LocalDateTime lastAccessDateFrom;

	private LocalDateTime lastAccessDateTo;

	public FileMetaDataSearchCriteria() {

	}

	public FileMetaDataSearchCriteria(String name, String descr, String path,
			LocalDateTime createdDateFrom, LocalDateTime createdDateTo,
			LocalDateTime lastUpdateDateFrom, LocalDateTime lastUpdateDateTo,
			String contentType, LocalDateTime lastAccessDateFrom,
			LocalDateTime lastAccessDateTo) {
		this.name = name;
		this.descr = descr;
		this.path = path;
		this.createdDateFrom = createdDateFrom;
		this.createdDateTo = createdDateTo;
		this.lastUpdateDateFrom = lastUpdateDateFrom;
		this.lastUpdateDateTo = lastUpdateDateTo;
		this.contentType = contentType;
		this.lastAccessDateFrom = lastAccessDateFrom;
		this.lastAccessDateTo = lastAccessDateTo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public LocalDateTime getCreatedDateFrom() {
		return createdDateFrom;
	}

	public void setCreatedDateFrom(LocalDateTime createdDateFrom) {
		this.createdDateFrom = createdDateFrom;
	}

	public LocalDateTime getCreatedDateTo() {
		return createdDateTo;
	}

	public void setCreatedDateTo(LocalDateTime createdDateTo) {
		this.createdDateTo = createdDateTo;
	}

	public LocalDateTime getLastUpdateDateFrom() {
		return lastUpdateDateFrom;
	}

	public void setLastUpdateDateFrom(LocalDateTime lastUpdateDateFrom) {
		this.lastUpdateDateFrom = lastUpdateDateFrom;
	}

	public LocalDateTime getLastUpdateDateTo() {
		return lastUpdateDateTo;
	}

	public void setLastUpdateDateTo(LocalDateTime lastUpdateDateTo) {
		this.lastUpdateDateTo = lastUpdateDateTo;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public LocalDateTime getLastAccessDateFrom() {
		return lastAccessDateFrom;
	}

	public void setLastAccessDateFrom(LocalDateTime lastAccessDateFrom) {
		this.lastAccessDateFrom = lastAccessDateFrom;
	}

	public LocalDateTime getLastAccessDateTo() {
		return lastAccessDateTo;
	}

	public void setLastAccessDateTo(LocalDateTime lastAccessDateTo) {
		this.lastAccessDateTo = lastAccessDateTo;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("name", name).append("descr", descr)
				.append("path", path)
				.append("createdDateFrom", createdDateFrom)
				.append("createdDateTo", createdDateTo)
				.append("lastUpdateDateFrom", lastUpdateDateFrom)
				.append("lastUpdateDateTo", lastUpdateDateTo)
				.append("contentType", contentType)
				.append("lastAccessDateFrom", lastAccessDateFrom)
				.append("lastAccessDateTo", lastAccessDateTo).toString();
	}
}
