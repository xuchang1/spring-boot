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

import org.springframework.web.bind.annotation.*;
import smoketest.tomcat.entity.Person;
import smoketest.tomcat.exception.CustomException;
import smoketest.tomcat.service.HelloWorldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
	public String helloWorld3(@RequestParam String id) {
		return id;
	}

	@PostMapping("/test4")
	@ResponseBody
	public Person helloWorld4(@RequestBody Person person) {
		return person;
	}

	@GetMapping("/test5")
	@ResponseBody
	public Person helloWorld5(@RequestParam String id) {
		Person person = new Person();
		person.setName(id);
		return person;
	}

	// 直接返回string时，会被 ViewNameMethodReturnValueHandler 解析，设置为视图名称，从而显示对应页面
	@GetMapping("/test6")
	public String helloWorld6() {
		return "index.html";
	}

	// 未添加 @RequestParam 注解，RequestParamMethodArgumentResolver 在第二次传参为true时解析了该入参
	@GetMapping("/test7")
	@ResponseBody
	public String helloWorld7(String id) {
		return id;
	}

	@GetMapping("/testException")
	@ResponseBody
	public String helloWorld8(String id) {
		throw new CustomException("A01", "异常测试");
	}
}
