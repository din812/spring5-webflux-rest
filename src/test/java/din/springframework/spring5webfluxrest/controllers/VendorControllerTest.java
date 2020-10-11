package din.springframework.spring5webfluxrest.controllers;

import din.springframework.spring5webfluxrest.domain.Category;
import din.springframework.spring5webfluxrest.domain.Vendor;
import din.springframework.spring5webfluxrest.repositories.CategoryRepository;
import din.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class VendorControllerTest {

    public static final String VENDOR_FIRST_NAME = "Some Vendor first name";
    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);

        vendorController = new VendorController(vendorRepository);

        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void list() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().first_name("Ven1").build(),
                        Vendor.builder().first_name("Ven2").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendorById() {
        BDDMockito.given(vendorRepository.findById("someid"))
                .willReturn(Mono.just(Vendor.builder().first_name("Ven1").build()));

        webTestClient.get()
                .uri("/api/v1/vendors/someid")
                .exchange()
                .expectBodyList(Vendor.class);
    }
    @Test
    public void testCreateVendor() {
        BDDMockito.given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> venToSaveMono = Mono.just(Vendor.builder().first_name(VENDOR_FIRST_NAME).build());

        webTestClient.post()
                .uri("/api/v1/vendors")
                .body(venToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdateVendor() {
        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToSaveMono = Mono.just(Vendor.builder().first_name(VENDOR_FIRST_NAME).build());

        webTestClient.put()
                .uri("/api/v1/vendors/123")
                .body(venToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void testPatchVendor() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().first_name("somefirstname").build()));

        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToSaveMono = Mono.just(Vendor.builder().first_name(VENDOR_FIRST_NAME).build());

        webTestClient.patch()
                .uri("/api/v1/vendors/123")
                .body(venToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(vendorRepository).save(any());
    }

    @Test
    public void testPatchVendorNoChanges() {
        BDDMockito.given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().first_name(VENDOR_FIRST_NAME).last_name(" ").build()));

        BDDMockito.given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToSaveMono = Mono.just(Vendor.builder().first_name(VENDOR_FIRST_NAME).last_name(" ").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/123")
                .body(venToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        BDDMockito.verify(vendorRepository, Mockito.never()).save(any());
    }
}