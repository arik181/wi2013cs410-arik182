#!/bin/sh
# This load script is a variant of a script pulled 
# shamelessly from page 47 of the book 'Linux Device Drivers'
# third edition, OReilly Press.
module="a4dev"
device="a4dev"
mode="664"
numdevices="0 1 2 3 4"

# Invoke insmod with all arguments we got
# and use a pathname, as newer modutils don't 
# look in, by default.
/sbin/insmod ./${module}.ko $* || exit 1

# remove stale nodes
rm -f /dev/${device}[0-4]

major=$(awk "\$2==\"$module\" {print \$1}" /proc/devices)


for i in $numdevices
do
    mknod /dev/${device}${i} c $major $i
done

# Give appropriate group/permissions, and chgrp.
# Not all distributions have staff, some have "wheel" instead.
group="staff"
grep -q '^staff:' /etc/group || group="wheel"

chgrp $group /dev/${device}[0-4]
chmod $mode  /dev/${device}[0-4]
