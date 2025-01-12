package com.example.firstproject;

import com.example.firstproject.repository.MemberRepository;
import com.example.firstproject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirstprojectApplication {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	PostRepository postRepository;

	public static void main(String[] args) {
		SpringApplication.run(FirstprojectApplication.class, args);
	}

//	@PostConstruct
//	public void init() {
//		Member member = new Member("test", "test1234", "테스터");
//
//		for(int i = 0; i < 150; i++) {
//			Post post = new Post("test" + i, "test content" + i, "tester");
//
//			postRepository.save(post);
//		}
//
//		memberRepository.add(member);
//	}
}
