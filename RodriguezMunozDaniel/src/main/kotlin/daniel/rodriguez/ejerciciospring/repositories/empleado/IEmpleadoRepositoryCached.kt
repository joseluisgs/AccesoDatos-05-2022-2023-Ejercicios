package daniel.rodriguez.ejerciciospring.repositories.empleado

import daniel.rodriguez.ejerciciospring.models.Empleado
import daniel.rodriguez.ejerciciospring.repositories.ICRUDRepository

interface IEmpleadoRepositoryCached : ICRUDRepository<Empleado, Long>