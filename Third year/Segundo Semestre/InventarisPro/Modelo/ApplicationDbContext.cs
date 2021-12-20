using InventarisPro.AlgoritmoGenetico;
using InventarisPro.Modelo.Entidades;
using InventarisPro.Modelo.ViewModels.Alarma;
using InventarisPro.Modelo.ViewModels.Categoria;
using InventarisPro.Modelo.ViewModels.Dashboard;
using InventarisPro.Modelo.ViewModels.Trabajador;
using Microsoft.EntityFrameworkCore;

namespace InventarisPro.Modelo
{
    public partial class ApplicationDbContext : DbContext
    {
        private readonly IConfiguration config;
        public ApplicationDbContext(IConfiguration config) => this.config = config;

        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options, IConfiguration config) : base(options)
        {
            this.config = config;
        }

        public virtual DbSet<DashboardViewModel> CantTotalCateQuery { get; set; }
        public virtual DbSet<CategoriaCantProdCateViewModel> CantProdCateQuery { get; set; }
        public virtual DbSet<VentaViewModel> CategoriaQuery { get; set; }
        public virtual DbSet<VentasViewModel> Ventas { get; set; }
        public virtual DbSet<ProductoGen> ProductoGens { get; set; }
        public virtual DbSet<Accion> Accions { get; set; } = null!;
        public virtual DbSet<Categoria> Categoria { get; set; } = null!;
        public virtual DbSet<Detalle> Detalles { get; set; } = null!;
        public virtual DbSet<Historial> Historials { get; set; } = null!;
        public virtual DbSet<Negocio> Negocios { get; set; } = null!;
        public virtual DbSet<Producto> Productos { get; set; } = null!;
        public virtual DbSet<Alarma> Alarma { get; set; } = null!;
        public virtual DbSet<CantidadAlarmas> CantidadAlarmas { get; set; } = null!;
        public virtual DbSet<Rol> Rols { get; set; } = null!;
        public virtual DbSet<Trabajador> Trabajadors { get; set; } = null!;
        public virtual DbSet<TrabajadorViewModel> Empleado { get; set; } = null!;
        public virtual DbSet<Venta> Venta { get; set; } = null!;

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseNpgsql(config["ConnectionStrings:ConnectionApplicationDbContext"]);
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<ProductoGen>(entity =>
            {
                entity.HasNoKey();
            });
            modelBuilder.Entity<VentasViewModel>(entity =>
            {
                entity.HasNoKey();
            });
            modelBuilder.Entity<DashboardViewModel>(entity =>
            {
                entity.HasNoKey();
            }); 
            modelBuilder.Entity<TrabajadorViewModel>(entity =>
            {
                entity.HasNoKey();
            }); 
            modelBuilder.Entity<CantidadAlarmas>(entity =>
            {
                entity.HasNoKey();
            });

            modelBuilder.Entity<Alarma>(entity =>
            {
                entity.HasNoKey();
            });

            modelBuilder.Entity<CategoriaCantProdCateViewModel>(entity =>
            {
                entity.HasNoKey();
            });

            modelBuilder.Entity<VentaViewModel>(entity =>
            {
                entity.HasNoKey();
            });

            modelBuilder.Entity<Accion>(entity =>
            {
                entity.HasKey(e => e.IdAccion).HasName("Accion_pkey");
            });

            modelBuilder.Entity<Categoria>(entity =>
            {
                entity.HasKey(e => e.IdCategoria).HasName("Categoria_pkey");
            });

            modelBuilder.Entity<Detalle>(entity =>
            {
                entity.HasKey(e => new { e.IdProducto, e.IdVenta, e.IdNegocio }).HasName("Detalle_pkey");

                entity.HasOne(d => d.IdNegocioNavigation)
                    .WithMany(p => p.Detalles)
                    .HasForeignKey(d => d.IdNegocio)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("NegocioFK");

                entity.HasOne(d => d.IdProductoNavigation)
                    .WithMany(p => p.Detalles)
                    .HasForeignKey(d => d.IdProducto)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("ProductoFK");

                entity.HasOne(d => d.IdVentaNavigation)
                    .WithMany(p => p.Detalles)
                    .HasForeignKey(d => d.IdVenta)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("VentaFK");
            });

            modelBuilder.Entity<Historial>(entity =>
            {
                entity.HasKey(e => new { e.IdTrabajador, e.IdProducto, e.IdAccion, e.FechaModificacion }).HasName("HistorialPK");

                entity.HasOne(d => d.IdAccionNavigation)
                    .WithMany(p => p.Historials)
                    .HasForeignKey(d => d.IdAccion)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("AccionFK");

                entity.HasOne(d => d.IdProductoNavigation)
                    .WithMany(p => p.Historials)
                    .HasForeignKey(d => d.IdProducto)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("ProductoFK");

                entity.HasOne(d => d.IdTrabajadorNavigation)
                    .WithMany(p => p.Historials)
                    .HasForeignKey(d => d.IdTrabajador)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("TrabajadorFK");
            });

            modelBuilder.Entity<Negocio>(entity =>
            {
                entity.HasKey(e => e.IdNegocio).HasName("Negocio_pkey");
            });

            modelBuilder.Entity<Producto>(entity =>
            {
                entity.HasKey(e => e.IdProducto).HasName("ProductoPK");

                entity.HasOne(d => d.IdCategoriaNavigation)
                    .WithMany(p => p.Productos)
                    .HasForeignKey(d => d.IdCategoria)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("CategoriaFK");

                entity.HasOne(d => d.IdNegocioNavigation)
                    .WithMany(p => p.Productos)
                    .HasForeignKey(d => d.IdNegocio)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("NegocioFK");
            });

            modelBuilder.Entity<Rol>(entity =>
            {
                entity.HasKey(e => e.IdRol).HasName("Rol_pkey");
            });

            modelBuilder.Entity<Trabajador>(entity =>
            {
                entity.HasKey(e => e.Rut).HasName("Trabajador_pkey");
                entity.HasOne(d => d.IdNegocioNavigation)
                    .WithMany(p => p.Trabajadors)
                    .HasForeignKey(d => d.IdNegocio)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("NegocioFK");

                entity.HasOne(d => d.IdRolNavigation)
                    .WithMany(p => p.Trabajadors)
                    .HasForeignKey(d => d.IdRol)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("RolFK");
            });

            modelBuilder.Entity<Venta>(entity =>
            {
                entity.HasKey(e => e.IdVenta).HasName("VentaPK");

                entity.HasOne(d => d.IdTrabajadorNavigation)
                    .WithMany(p => p.Ventas)
                    .HasForeignKey(d => d.IdTrabajador)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("TrabajadorFK");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
