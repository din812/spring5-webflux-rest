package din.springframework.spring5webfluxrest.controllers;

import din.springframework.spring5webfluxrest.domain.Category;
import din.springframework.spring5webfluxrest.domain.Vendor;
import din.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("/api/v1/vendors/")
    public Flux<Vendor> list() {
        return vendorRepository.findAll();
    }

    @GetMapping("/api/v1/vendors/{id}")
    public Mono<Vendor> getVendorById(@PathVariable String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/vendors")
    Mono<Void> create(@RequestBody Publisher<Vendor> vendorPublisher) {
        return vendorRepository.saveAll(vendorPublisher).then();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/api/v1/vendors/{id}")
    Mono<Vendor> update(@PathVariable String id, @RequestBody Vendor vendor) {
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/api/v1/vendors/{id}")
    Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {
        Vendor foundVendor = vendorRepository.findById(id).block();

        if (!foundVendor.getFirst_name().equals(vendor.getFirst_name())) {
            foundVendor.setFirst_name(vendor.getFirst_name());
            return vendorRepository.save(foundVendor);
        } else if (!foundVendor.getLast_name().equals(vendor.getLast_name())) {
            foundVendor.setLast_name(vendor.getLast_name());
            return vendorRepository.save(foundVendor);
        }
        return Mono.just(foundVendor);
    }
}
