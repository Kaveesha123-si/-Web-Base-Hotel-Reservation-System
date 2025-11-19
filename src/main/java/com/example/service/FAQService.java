package com.example.service;

import com.example.model.FAQ;
import com.example.repository.FAQRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FAQService {
    
    @Autowired
    private FAQRepository faqRepository;
    
    public List<FAQ> getAllFAQs() {
        return faqRepository.findAll();
    }
    
    public List<FAQ> getActiveFAQs() {
        return faqRepository.findByActiveTrueOrderByDisplayOrderAsc();
    }
    
    public Optional<FAQ> getFAQById(Long id) {
        return faqRepository.findById(id);
    }
    
    public FAQ saveFAQ(FAQ faq) {
        return faqRepository.save(faq);
    }
    
    public FAQ updateFAQ(FAQ faq) {
        return faqRepository.save(faq);
    }
    
    public void deleteFAQ(Long id) {
        faqRepository.deleteById(id);
    }
    
    public List<FAQ> getFAQsByCategory(FAQ.Category category) {
        return faqRepository.findByCategoryAndActiveTrue(category);
    }
    
    public List<FAQ> searchFAQs(String question) {
        return faqRepository.findByQuestionContainingIgnoreCaseAndActiveTrue(question);
    }
}

