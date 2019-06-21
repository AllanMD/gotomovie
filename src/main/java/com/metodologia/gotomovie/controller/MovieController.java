package com.metodologia.gotomovie.controller;

import com.metodologia.gotomovie.domain.Movie;
import com.metodologia.gotomovie.domain.SearchResults;
import com.metodologia.gotomovie.repository.MovieRepository;
import com.metodologia.gotomovie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

// movie api: https://developers.themoviedb.org/3/getting-started/introduction
@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    MovieRepository movieRepository;
    MovieService movieService = new MovieService(); // ver esto

    @GetMapping("")
    public Movie getMovie(){
        return new Movie();
    }

    @GetMapping("search/{title}")
    public ModelAndView getMoviesByTitle(@PathVariable String title) {
        RestTemplate restTemplate = new RestTemplate();
        SearchResults searchResults = movieService.getMoviesByTitle(title);
        List<Movie> movies = new ArrayList<Movie>();
        searchResults.getResults().forEach(movie -> {
            movies.add(movie);
        });
        ModelAndView model = new ModelAndView();
        model.addObject("movies", movies);
        model.setViewName("movie");
        return model;
    }

    @PostMapping("/search2")
    public ModelAndView getMoviesByTitle2(Movie movieAux) {
        RestTemplate restTemplate = new RestTemplate();
        SearchResults searchResults = movieService.getMoviesByTitle(movieAux.getTitle());
        List<Movie> movies = new ArrayList<Movie>();
        searchResults.getResults().forEach(movie -> {
            movies.add(movie);
        });
        ModelAndView model = new ModelAndView();
        model.addObject("movies", movies);
        model.addObject("movie", new Movie());
        model.setViewName("movie");
        return model;
    }

    @PostMapping("")
    public Movie addMovie(@RequestBody Movie movie){
        return movieRepository.save(movie);
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable int id) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("https://api.themoviedb.org/3/movie/"+id+"?api_key=8d7db92be0746d3da167842d227f2f64&language=en-US", Movie.class);
    }


    @PostMapping("/buscar")
    public ModelAndView buscar(Movie movie) {
        movie = this.getMovieById(movie.getId());
        System.out.println(movie.toString());
        ModelAndView model = new ModelAndView();
        model.addObject("movie",movie);
        model.setViewName("movie");
        return model;
    }
}

