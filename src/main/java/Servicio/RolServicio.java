package Servicio;

import DAO.RolDAO;
import Modelo.Rol;
import java.util.List;


public class RolServicio {
    
        public List<Rol> getRoles() {
        return RolDAO.obtenerRoles();
    }
}
 