package lv.celokopa.app.services;

import lv.celokopa.app.dao.LocalityRepository;
import lv.celokopa.app.model.Locality;
import lv.celokopa.app.model.SearchResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by alex on 16.10.5.
 */
@Service
public class LocalityService {
    private static final Logger LOGGER = Logger.getLogger(LocalityService.class);

    @Autowired
    LocalityRepository localityRepository;

    @Transactional(readOnly = true)
    public SearchResult<Locality> findLocalities(String predicate) {

        List<Locality> localities = localityRepository.findLocalities(predicate);

        return new SearchResult<>(localities.size(), localities);
    }
}
