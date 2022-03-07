#
# Copyright 2021 Micro Focus or one of its affiliates.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

FROM dockerhub-public.svsartifactory.swinfra.net/cafapi/opensuse-jdk11:3.4.3

RUN zypper -n install libicu wget
RUN rpm --import https://packages.microsoft.com/keys/microsoft.asc
RUN wget https://packages.microsoft.com/config/opensuse/15/prod.repo
RUN mv prod.repo /etc/zypp/repos.d/microsoft-prod.repo
RUN chown root:root /etc/zypp/repos.d/microsoft-prod.repo
RUN zypper -n install dotnet-sdk-5.0
