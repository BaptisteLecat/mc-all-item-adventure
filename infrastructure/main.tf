resource "google_compute_instance" "mc_server" {
  name         = "mc-server"
  machine_type = "e2-small"
  zone         = var.zone
  tags         = ["minecraft-server"]

  boot_disk {
    initialize_params {
      image = "cos-cloud/cos-stable"
      size  = 10
    }
  }

  network_interface {
    network    = "default"
    subnetwork = "default"

    access_config {
      // Permet d'avoir une IP publique
    }
  }

  metadata = {
    google-logging-enabled    = "true"
    google-monitoring-enabled = "true"
    startup-script            = <<EOT
#!/bin/bash
set -x  # Debug mode

# Attendre que /dev/sdb soit bien attaché
while [ ! -e /dev/sdb ]; do sleep 1; done

# Vérifie si le disque est déjà formaté
if ! blkid /dev/sdb; then
  sudo mkfs.ext4 -F /dev/sdb
fi

# Monter le disque
sudo mkdir -p /mnt/disks/data
sudo mount /dev/sdb /mnt/disks/data
sudo chmod 777 /mnt/disks/data
echo "/dev/sdb /mnt/disks/data ext4 defaults 0 0" | sudo tee -a /etc/fstab
EOT

    gce-container-declaration = <<EOT
spec:
  containers:
  - name: mc-container
    image: europe-west1-docker.pkg.dev/weatherapp-journey/mc-all-item-adventure/mc-all-item-adventure:0.0.1
    env:
    - name: EULA
      value: 'TRUE'
    volumeMounts:
    - name: host-path-0
      readOnly: false
      mountPath: /data
    stdin: true
    tty: true
  volumes:
  - name: host-path-0
    hostPath:
      path: /mnt/disks/data
  restartPolicy: Always
EOT
  }

  service_account {
    email  = "53131514713-compute@developer.gserviceaccount.com"
    scopes = ["cloud-platform"]
  }

  attached_disk {
    device_name = "persistent-disk-1"
    mode        = "READ_WRITE"
    source      = google_compute_disk.mc_data_disk.self_link
  }

  scheduling {
    automatic_restart   = true
    on_host_maintenance = "MIGRATE"
  }
}

resource "google_compute_disk" "mc_data_disk" {
  name = "mc-data-disk"
  type = "pd-standard"
  zone = var.zone
  size = 10
}

resource "google_compute_attached_disk" "attach_disk" {
  disk     = google_compute_disk.mc_data_disk.id
  instance = google_compute_instance.mc_server.id
}

resource "google_compute_firewall" "mc_server_firewall" {
  name          = "mc-server-firewall"
  network       = "default"
  target_tags   = ["minecraft-server"]
  source_ranges = ["0.0.0.0/0"]
  allow {
    protocol = "tcp"
    ports    = ["25565"]
  }
}

resource "google_artifact_registry_repository" "mc_all_item_adventure_repository" {
  location      = var.region
  repository_id = var.artifact_registry_repository
  format        = "DOCKER"
}
