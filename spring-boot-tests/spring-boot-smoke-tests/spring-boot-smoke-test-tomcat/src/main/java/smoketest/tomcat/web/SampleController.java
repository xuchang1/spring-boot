/*
 * Copyright 2012-2019 the original author or authors.
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

package smoketest.tomcat.web;

import org.springframework.web.bind.annotation.PathVariable;
import smoketest.tomcat.service.HelloWorldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {

	@Autowired
	private HelloWorldService helloWorldService;

	@GetMapping({"/test", "/test1", "/test/${server.servlet.context-path}"})
	@ResponseBody
	public String helloWorld() {
		return this.helloWorldService.getHelloMessage();
	}

	@GetMapping("/test2/{id}")
	@ResponseBody
	public String helloWorld2(@PathVariable String id) {
		return id;
	}

	@GetMapping("/test3")
	@ResponseBody
	public String helloWorld3(@PathVariable String id) {
		return id;
	}
}
