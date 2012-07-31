import org.springframework.security.acls.domain.DefaultPermissionFactory
import com.dtolabs.yana2.springacl.YanaPermission

// Place your Spring DSL code here
beans = {
    aclPermissionFactory(DefaultPermissionFactory, YanaPermission)
}
