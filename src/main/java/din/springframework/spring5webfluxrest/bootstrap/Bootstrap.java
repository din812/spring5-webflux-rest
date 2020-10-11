package din.springframework.spring5webfluxrest.bootstrap;

import din.springframework.spring5webfluxrest.domain.Category;
import din.springframework.spring5webfluxrest.domain.Vendor;
import din.springframework.spring5webfluxrest.repositories.CategoryRepository;
import din.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (categoryRepository.count().block() == 0) {

            System.out.println("### LOADING DATA ON BOOTSTRAP ###");

            populateCategory();

            System.out.println("Loaded Categories: " + categoryRepository.count().block());

            populateVendor();

            System.out.println("Loaded Vendors: " + vendorRepository.count().block());
        } else {
            System.out.println("### DATABASE IS ALREADY POPULATED ###");
            System.out.println("Categories: " + categoryRepository.count().block());
            System.out.println("Vendors: " + vendorRepository.count().block());
        }


    }

    private void populateCategory() {
        Category intel = new Category();
        intel.setDescription("Intel CPU's");

        categoryRepository.save(intel).block();

        Category amd = new Category();
        amd.setDescription("AMD CPU's");

        categoryRepository.save(amd).block();
    }

    private void populateVendor() {
        Vendor den = new Vendor();
        den.setFirst_name("Den");
        den.setLast_name("Soma_lastname");

        vendorRepository.save(den).block();

        Vendor kent = new Vendor();
        kent.setFirst_name("Kent");
        kent.setLast_name("Kentich");

        vendorRepository.save(kent).block();
    }
}
