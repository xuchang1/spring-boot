/*
 * Copyright 2012-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.loader;

import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.Archive.EntryFilter;

/**
 * {@link Launcher} for JAR based archives. This launcher assumes that dependency jars are
 * included inside a {@code /BOOT-INF/lib} directory and that application classes are
 * included inside a {@code /BOOT-INF/classes} directory.
 *
 * @author Phillip Webb
 * @author Andy Wilkinson
 * @author Madhura Bhave
 * @author Scott Frederick
 * @since 1.0.0
 */
public class JarLauncher extends ExecutableArchiveLauncher {

	// 获得，BOOT-INF/classes/ 目录下的类，以及 BOOT-INF/lib/ 的内嵌 jar 包
	static final EntryFilter NESTED_ARCHIVE_ENTRY_FILTER = (entry) -> {
		// 如果是目录的情况，只要 BOOT-INF/classes/ 目录
		if (entry.isDirectory()) {
			return entry.getName().equals("BOOT-INF/classes/");
		}
		// 如果是文件的情况，只要 BOOT-INF/lib/ 目录下的 `jar` 包
		return entry.getName().startsWith("BOOT-INF/lib/");
	};

	public JarLauncher() {
	}

	protected JarLauncher(Archive archive) {
		super(archive);
	}

	@Override
	protected boolean isPostProcessingClassPathArchives() {
		return false;
	}

	@Override
	protected boolean isNestedArchive(Archive.Entry entry) {
		return NESTED_ARCHIVE_ENTRY_FILTER.matches(entry);
	}

	@Override
	protected String getArchiveEntryPathPrefix() {
		return "BOOT-INF/";
	}

	public static void main(String[] args) throws Exception {
		new JarLauncher().launch(args);
	}

}
