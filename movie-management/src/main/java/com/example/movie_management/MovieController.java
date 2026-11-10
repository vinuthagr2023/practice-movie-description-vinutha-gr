package com.example.movie_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.web.bind.annotation.*;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.apache.http.HttpException;
import java.io.IOException;


import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
@RequestMapping("/movies")
public class MovieController {

    String apiKey = System.getenv("GOOGLE_API_KEY");
    Client client = Client.builder().apiKey(apiKey).build();

    public MovieController()
    {
        System.out.println(apiKey != null);
    }

     @Autowired
    MovieRepository movieRepository;

    @GetMapping
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    @PostMapping
    public Movie addMovie(@RequestBody Movie movie) {

        String prompt = "Write a short, interesting description of the movie "
                + movie.getTitle() + " in 2-3 sentences.";


        GenerateContentResponse response = client.models.generateContent(
                "gemini-3.5-flash-lite",
                prompt,
                null
        );

        movie.setDescription(response.text());

            return movieRepository.save(movie);


    }
}
