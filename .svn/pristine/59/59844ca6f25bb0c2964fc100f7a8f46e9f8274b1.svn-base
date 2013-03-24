// A4 Device Driver
// CS 410
// Devin Quirozoliver

#include <linux/init.h>
#include <linux/module.h>
#include <linux/types.h>
#include <linux/kdev_t.h>
#include <linux/fs.h>
#include <linux/cdev.h>
#include <linux/usb.h>
#include <linux/slab.h>
#include <linux/pci.h>
//#include <linux/ioport.h>

#define DEVCNT 5
#define STRINGSIZE 12
#define DEVNAME "a4dev"

static int       a4_open(struct inode *inode, 
                         struct file *file);
static ssize_t   a4_read(struct file  *file,
                         char __user *buf,
                         size_t len,
                         loff_t  *offset);
static ssize_t  a4_write(struct file  *file,  
                         const char __user *buf,
                         size_t len,
                         loff_t  *offset);
static int      a4_pci_probe(struct pci_dev *pdev, 
                             const struct pci_device_id *ent);
static void     a4_pci_remove(struct pci_dev *pdev);

// Device struct
struct a4dev_dev {
  struct cdev cdev;
  int sys_int;
  u32 read_count;
} a4dev;

// File operations for a4 device
struct file_operations a4dev_fops = {
  .owner = THIS_MODULE,
  .open  = a4_open,
  .read  = a4_read,
  .write = a4_write,
};

// Device Node
static dev_t a4dev_node;

// lazy global variable...
static u8 __iomem *hw_addr;

// device ID table
static struct pci_device_id a4_pci_table[] = {
  { PCI_DEVICE(0x8086, 0x100E) },
  { } // terminating entry - this is required
};

MODULE_DEVICE_TABLE(pci, a4_pci_table);

static struct pci_driver a4_pci_driver = {
  .name = "a4dev",
  .id_table = a4_pci_table,
  .probe = a4_pci_probe,
  .remove = a4_pci_remove,
};

// Open
static int a4_open(struct inode *inode, struct file *file) {
  printk(KERN_INFO "%s: opened\n", DEVNAME);
  a4dev.sys_int = 15;
  a4dev.read_count = 0;
  return 0;
}

// Read
static ssize_t a4_read(struct file *file, 
                       char __user *buf,
                       size_t len, 
                       loff_t *offset) {
    // Get a local kernel buffer set aside
  ssize_t ret = 0;
  u32 reg = 0x0;
  //int eof = -1;
  char string[STRINGSIZE];
  int i = 0;

    // Make sure our user wasn't bad...
  if (!buf) {
    ret = -EINVAL;
    goto out;
  }

  // Give the sys_init to userspace
  //if (copy_to_user(buf, &a4dev.sys_int, sizeof(int))) {
  //  ret = -EFAULT;
  //  goto out;
  //}

  printk(KERN_INFO "%s: user got from us %d\n", DEVNAME, a4dev.sys_int);
  printk(KERN_INFO "%s: hw_addr is set to 0x%x\n", DEVNAME, (u32) hw_addr);

  // Read the PCI CTRL register from offset 0x0000
  reg = readl(hw_addr + 0x0);
  printk(KERN_INFO "%s: register value is 0x%x\n", DEVNAME, (u32) reg);

  ret = (ssize_t) 12;

  // Convert the register to a useful string
  for (i=0; i<STRINGSIZE; ++i) {
    string[i] = '\0';
  }
  sprintf(string, "0x%x", reg);

  // Copy the register value back to userspace
  if (copy_to_user(buf, &string, STRINGSIZE)) {
    ret = -EFAULT;
    goto out;
  }

  //if (copy_to_user(buf, &reg, sizeof(u32))) {
  ////  ret = -EFAULT;
  //  goto out;
  //}
  //if (copy_to_user(buf, &eof, sizeof(u32))) {
  //  ret = -EFAULT;
  //  goto out;
  //}


  // Increment number of times read has been called
  //++a4dev.read_count;
  //ret = a4dev.read_count;

out:
  return ret;
}

// Write
static ssize_t a4_write(struct file *file, 
                        const char __user *buf,
                        size_t len, 
                        loff_t *offset) {
  // Have local kernel memory ready
  char *kern_buf;
  int ret;

  // Make sure our user isn't bad...
  if (!buf) {
    ret = -EINVAL;
    goto out;
  }

  // Get some memory to copy into...
  kern_buf = kmalloc(len, GFP_KERNEL);

  // ...and make sure it's good to go
  if (!kern_buf) {
    ret = -ENOMEM;
    goto out;
  }

  // Copy from the user-provided buffer
  if (copy_from_user(kern_buf, buf, len)) {
    ret = -EFAULT;
    goto free;
  }

  ret = len;

  // print what userspace gave us
  printk(KERN_INFO "%s: userspace wrote \"%s\" to us\n", DEVNAME, kern_buf);

free:
  kfree(kern_buf);
out:
  return ret;
}

// Probe PCI
static int __devinit a4_pci_probe(struct pci_dev *pdev,
                                  const struct pci_device_id *ent)
{
  printk(KERN_INFO "%s: probe invoked\n", DEVNAME);

  pci_request_selected_regions(pdev,
                               pci_select_bars(pdev, IORESOURCE_MEM),
                               "a4_pci");

  hw_addr = ioremap(pci_resource_start(pdev, 0),
                    pci_resource_len(pdev, 0));

  //printk(KERN_INFO "%s: hw_addr is set to 0x%x\n", DEVNAME, (u32) hw_addr);
  return 0;
}

// Unload PCI
static void __devexit a4_pci_remove(struct pci_dev *pdev)
{
  iounmap(hw_addr);
  pci_release_selected_regions(pdev,
                               pci_select_bars(pdev, IORESOURCE_MEM));

  return;
}

// Initialize device driver
static int __init a4_init(void)
{
  int ret = 0;
  u32 a4_major = 0;
  u32 a4_minor = 0;
  printk(KERN_INFO "%s: loading...\n", DEVNAME);

  // Allocate and register device driver
  a4dev_node = MKDEV(a4_major, a4_minor);
  if (alloc_chrdev_region(&a4dev_node, 
                          a4_minor, 
                          DEVCNT, 
                          DEVNAME)) {
      printk(KERN_WARNING "%s: can't get major %d\n", 
             DEVNAME, 
             a4_major);
      ret = -1;
      goto fail;
  }
  a4_major = MAJOR(a4dev_node);
  printk(KERN_INFO "%s: allocated %d devices at major: %d\n", 
         DEVNAME,
         DEVCNT, 
         a4_major);

  // Initialize and add cdev
  cdev_init(&a4dev.cdev, &a4dev_fops);
  a4dev.cdev.owner = THIS_MODULE;
  if (cdev_add(&a4dev.cdev, a4dev_node, DEVCNT)) {
      printk(KERN_ERR "%s: Failed cdev_add()!\n", DEVNAME);
      goto dealloc;
  }

  ret = pci_register_driver(&a4_pci_driver);
  if (ret != 0) goto fail;

  return 0;

dealloc:
  unregister_chrdev_region(a4dev_node, DEVCNT);
fail: 
  return ret;
}

// Unload device driver
static void __exit a4_exit(void)
{
  pci_unregister_driver(&a4_pci_driver);
  cdev_del(&a4dev.cdev);
  unregister_chrdev_region(a4dev_node, DEVCNT);
  printk(KERN_INFO "a4dev unloaded.\n");
}

// Lifted from StackOverflow. :)
int sprintf(char * buf, const char * fmt, ...) 
{
  va_list args;
  int i;

  va_start(args, fmt);
  i=vsprintf(buf,fmt,args);
  va_end(args);
  return i;
}

MODULE_AUTHOR("Devin Quirozoliver");
MODULE_LICENSE("GPL");
MODULE_VERSION("0.1");
module_init(a4_init);
module_exit(a4_exit);
