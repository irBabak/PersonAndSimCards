package mft.model.service;

import lombok.Getter;
import mft.model.entity.SimCard;
import mft.model.repository.SimCardRepository;

import java.util.List;

public class SimCardService {
    @Getter
    private static SimCardService simCardService = new SimCardService();

    private SimCardService() {}

    public void save(SimCard simCard) throws Exception {
        try (SimCardRepository repository = new SimCardRepository()) {
            repository.save(simCard);
        }
    }

    public void edit(SimCard simCard) throws Exception {
        try (SimCardRepository repository = new SimCardRepository()) {
            repository.edit(simCard);
        }
    }

    public void remove(int id) throws Exception {
        try (SimCardRepository repository = new SimCardRepository()) {
            repository.remove(id);
        }
    }

    public List<SimCard> findAll() throws Exception {
        try (SimCardRepository repository = new SimCardRepository()) {
            return repository.findAll();
        }
    }

    public List<SimCard> findByPersonNationalCode(String nationalCode) throws Exception {
        try (SimCardRepository repository = new SimCardRepository()) {
            return repository.findByPersonNationalCode(nationalCode);
        }
    }

    public int countPersonSimCardsByNationalCode(String nationalCode) throws Exception {
        try (SimCardRepository repository = new SimCardRepository()) {
            return repository.countPersonSimCardsByNationalCode(nationalCode);
        }
    }
}
